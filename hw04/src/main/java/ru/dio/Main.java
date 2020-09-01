package ru.dio;

import com.sun.management.GarbageCollectionNotificationInfo;

import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;
import java.lang.management.*;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


public class Main {

    private static ArrayList<String> arrayList = new ArrayList<>();

    public static void main(String[] args) throws InterruptedException {
        switchOnMonitoring();
        oom();
    }

    private static void oom() throws InterruptedException {
        Random random = new Random();
        while (true) {
            int size = 1000;
            String[] array = new String[size];

            for (int i = 0; i < size; i++) {
                array[i] = new String(new char[0]);
            }

            if (random.nextBoolean()) {
                Collections.addAll(arrayList, array);
            } else {
                Thread.sleep(10);
            }
        }
    }

    private static void switchOnMonitoring() {
        List<GarbageCollectorMXBean> gcbeans = java.lang.management.ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gcbean : gcbeans) {
            System.out.println("GC name:" + gcbean.getName());
            NotificationEmitter emitter = (NotificationEmitter) gcbean;


            class Duration {
                private long duration;

                public void increaseDuration(long duration) {
                    this.duration += duration;
                }

                public long getDuration() {
                    return this.duration;
                }
            }

            class MaxDuration {
                private long duration;

                public void setMaxPause(long duration) {
                    this.duration = Math.max(this.duration, duration);
                }

                public long getMaxDuration() {
                    return this.duration;
                }
            }

            final Duration youngGenerationDuration = new Duration();
            final Duration oldGenerationDuration = new Duration();

            final MaxDuration youngGenerationMD = new MaxDuration();
            final MaxDuration oldGenerationMD = new MaxDuration();

            NotificationListener listener = (notification, handback) -> {
                if (notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
                    GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());
                    String gcName = info.getGcName();
                    String gcAction = info.getGcAction();
                    String gcCause = info.getGcCause();
                    long startTime = info.getGcInfo().getStartTime();
                    long duration = info.getGcInfo().getDuration();

                    //G1
                    String young = "G1 Young Generation";
                    String old = "G1 Old Generation";

                    //Serial
                    String copy = "Copy";
                    String markSweepCompact = "MarkSweepCompact";

                    //Parallel
                    String markSweep = "PS MarkSweep";
                    String scavenge = "PS Scavenge";

                    if (gcName.equals(markSweep)) {
                        youngGenerationDuration.increaseDuration(duration);
                        youngGenerationMD.setMaxPause(duration);
                        System.out.println(" Name:" + gcName + ", gcCause:" + gcCause + "(" + duration + " ms)"
                                + " all time " + youngGenerationDuration.getDuration() + " max: " + youngGenerationMD.getMaxDuration());
                    }

                    if (gcName.equals(scavenge)) {
                        oldGenerationDuration.increaseDuration(duration);
                        oldGenerationMD.setMaxPause(duration);
                        System.out.println(" Name:" + gcName + ", gcCause:" + gcCause + "(" + duration + " ms)"
                                + " all time " + oldGenerationDuration.getDuration() + " max: " + oldGenerationMD.getMaxDuration());
                    }



                }
            };
            emitter.addNotificationListener(listener, null, null);
        }
    }

}