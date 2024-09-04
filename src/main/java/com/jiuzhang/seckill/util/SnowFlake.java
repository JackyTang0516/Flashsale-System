package com.jiuzhang.seckill.util;

/**
 * Twitter's distributed self-incrementing ID snowflake algorithm snowflake (Java version)
 **/
public class SnowFlake {

    /**
     * Starting timestamp
     */
    private final static long START_STMP = 1480166465631L;

    /**
     * Number of bits occupied by each part
     */
    private final static long SEQUENCE_BIT = 12; //Number of bits occupied by the serial number
    private final static long MACHINE_BIT = 5;   //Number of bits occupied by the machine identification
    private final static long DATACENTER_BIT = 5;//Number of bits occupied by data centers

    /**
     * Maximum value for each part
     */
    private final static long MAX_DATACENTER_NUM = -1L ^ (-1L << DATACENTER_BIT);
    private final static long MAX_MACHINE_NUM = -1L ^ (-1L << MACHINE_BIT);
    private final static long MAX_SEQUENCE = -1L ^ (-1L << SEQUENCE_BIT);

    /**
     * Displacement of each part to the left
     */
    private final static long MACHINE_LEFT = SEQUENCE_BIT;
    private final static long DATACENTER_LEFT = SEQUENCE_BIT + MACHINE_BIT;
    private final static long TIMESTMP_LEFT = DATACENTER_LEFT + DATACENTER_BIT;

    private long datacenterId;  //data center
    private long machineId;     //machine identification
    private long sequence = 0L; //product key (software)
    private long lastStmp = -1L;//last timestamp

    public SnowFlake(long datacenterId, long machineId) {
        if (datacenterId > MAX_DATACENTER_NUM || datacenterId < 0) {
            throw new IllegalArgumentException("datacenterId can't be greater than MAX_DATACENTER_NUM or less than 0");
        }
        if (machineId > MAX_MACHINE_NUM || machineId < 0) {
            throw new IllegalArgumentException("machineId can't be greater than MAX_MACHINE_NUM or less than 0");
        }
        this.datacenterId = datacenterId;
        this.machineId = machineId;
    }

    /**
     * Generate the next ID
     *
     * @return
     */
    public synchronized long nextId() {
        long currStmp = getNewstmp();
        if (currStmp < lastStmp) {
            throw new RuntimeException("Clock moved backwards.  Refusing to generate id");
        }

        if (currStmp == lastStmp) {
            //Self-incrementing sequence number for the same milliseconds
            sequence = (sequence + 1) & MAX_SEQUENCE;
            //The number of sequences in the same millisecond has been maximized
            if (sequence == 0L) {
                currStmp = getNextMill();
            }
        } else {
            //Sequence number is set to 0 for different milliseconds
            sequence = 0L;
        }

        lastStmp = currStmp;

        return (currStmp - START_STMP) << TIMESTMP_LEFT //Timestamp section
                | datacenterId << DATACENTER_LEFT       //Data center component
                | machineId << MACHINE_LEFT             //Machine marking section
                | sequence;                             //Serial number part
    }

    private long getNextMill() {
        long mill = getNewstmp();
        while (mill <= lastStmp) {
            mill = getNewstmp();
        }
        return mill;
    }

    private long getNewstmp() {
        return System.currentTimeMillis();
    }

    public static void main(String[] args) {
        SnowFlake snowFlake = new SnowFlake(2, 1);

        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000000; i++) {
            System.out.println(snowFlake.nextId());
        }

        System.out.println("The total time consumption：" + (System.currentTimeMillis() - start));
    }
}