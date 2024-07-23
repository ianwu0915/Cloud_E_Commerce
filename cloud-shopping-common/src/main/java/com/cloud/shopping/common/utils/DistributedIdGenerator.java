package com.cloud.shopping.common.utils;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;

/**
 * Distributed ID Generator (Snowflake Algorithm)
 *
 * Structure:
 *
 * 1||0---0000000000 0000000000 0000000000 0000000000 0 --- 00000 ---00000 ---000000000000
 *
 * - 1 bit: Always 0 (reserved)
 * - 41 bits: Timestamp (milliseconds since epoch)
 * - 10 bits: Machine ID (5 bits datacenter + 5 bits worker)
 * - 12 bits: Sequence number (auto-incrementing)
 *
 * This implementation can generate unique IDs across distributed systems
 * while maintaining rough time order. Can generate approximately 260,000
 * unique IDs per second.
 *
 */
public class DistributedIdGenerator {
    private final static long twitterEpoch = 1288834974657L;
    // Number of bits for worker ID
    private final static long workerIdBits = 5L;
    // Number of bits for datacenter ID
    private final static long datacenterIdBits = 5L;
    // Maximum worker ID value (31)
    private final static long maxWorkerId = -1L ^ (-1L << workerIdBits);
    // Maximum datacenter ID value (31)
    private final static long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);
    // Number of bits for sequence
    private final static long sequenceBits = 12L;
    // Worker ID shift bits
    private final static long workerIdShift = sequenceBits;
    // Datacenter ID shift bits
    private final static long datacenterIdShift = sequenceBits + workerIdBits;
    // Timestamp shift bits
    private final static long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;

    private long sequence = 0L;
    // sequence mask
    private final static long sequenceMask = -1L ^ (-1L << sequenceBits);
    // worker ID
    private long lastTimestamp = -1L;
    // worker ID
    private long workerId;
    // datacenter ID
    private long datacenterId;
    // sequence

    /**
     * Creates a new ID generator with automatically determined worker and datacenter IDs
     */
    public DistributedIdGenerator() {
        this.datacenterId = getDatacenterId(maxDatacenterId);
        this.workerId = getWorkerId(datacenterId, maxWorkerId);
    }

    /**
     * Creates a new ID generator with specified worker and datacenter IDs
     *
     * @param workerId Worker node ID (0-31)
     * @param datacenterId Data center ID (0-31)
     * @throws IllegalArgumentException if IDs are out of valid range
     */
    public DistributedIdGenerator(long workerId, long datacenterId) {
        if (workerId > maxWorkerId || workerId <0) {
            throw new IllegalArgumentException(String.format("Worker ID can't be greater than %d or less than 0", maxWorkerId));
        }

        if (datacenterId > maxDatacenterId || datacenterId <0) {
            throw new IllegalArgumentException(String.format("Datacenter ID can't be greater than %d or less than 0", maxDatacenterId));
        }

        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    public synchronized long nextId() {
        long timeStamp = getCurrentTime();

        // If the time moves backwards, prevent the system from generating IDs
        if (timeStamp < lastTimestamp) {
            throw new RuntimeException("Clock moved backwards. Refusing to generate ID for " + (lastTimestamp - timeStamp) + " milliseconds");
        }

        // If the time is the same as the last time, increment the sequence
        if (lastTimestamp == timeStamp) {
            sequence = (sequence + 1) & sequenceMask;
            if (sequence == 0) {
                // If the sequence overflows (reaches 4096), it waits until the next millisecond.
                // cuz each millisecond can generate 4096 IDs
                // so it will create duplicate IDs if the sequence overflows to 0
                // By waiting to the next millisecond, it can guarantee the uniqueness of the ID (new 4096 IDs)
                timeStamp = tilNextMillis(lastTimestamp);
            }
            // If the time is different from the last time, reset the sequence
        } else {
            sequence = 0L;
        }

        lastTimestamp = timeStamp;
        long nextId = ((timeStamp - twitterEpoch) << timestampLeftShift) |
                (datacenterId << datacenterIdShift) |
                (workerId << workerIdShift) |
                sequence;

        return nextId;
    }

    private long tilNextMillis(long lastTimestamp) {
        long timeStamp = getCurrentTime();
        while (timeStamp <= lastTimestamp) {
            timeStamp = getCurrentTime();
        }
        return timeStamp;
    }

    public long getCurrentTime() {
        return System.currentTimeMillis();
    }

    /**
     * Generates a worker ID based on datacenter ID and JVM process ID
     *
     * @param datacenterId Current datacenter ID
     * @param maxWorkerId Maximum allowed worker ID
     * @return Generated worker ID
     */
    protected static long getWorkerId(long datacenterId, long maxWorkerId) {
        // mpid: MAC + PID
        StringBuffer mpid = new StringBuffer();
        mpid.append(datacenterId);
        String name = ManagementFactory.getRuntimeMXBean().getName();
        if (!name.isEmpty()) {
            /*
             * GET jvmPid
             */
            mpid.append(name.split("@")[0]);
        }
        /*
         * MAC + PID 的 hashcode 获取16个低位
         */
        return (mpid.toString().hashCode() & 0xffff) % (maxWorkerId + 1);
    }

    /**
     * Generates a datacenter ID based on network interface MAC address
     *
     * @param maxDatacenterId Maximum allowed datacenter ID
     * @return Generated datacenter ID (falls back to 1 if network interface unavailable)
     */
    protected static long getDatacenterId(long maxDatacenterId) {
        long id = 0L;
        try {
            InetAddress ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            if (network == null) {
                id = 1L;
            } else {
                byte[] mac = network.getHardwareAddress();
                id = ((0x000000FF & (long) mac[mac.length - 1])
                        | (0x0000FF00 & (((long) mac[mac.length - 2]) << 8))) >> 6;
                id = id % (maxDatacenterId + 1);
            }
        } catch (Exception e) {
            System.out.println(" getDatacenterId: " + e.getMessage());
        }
        return id;
    }


}
