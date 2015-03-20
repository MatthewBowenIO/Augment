package com.assembler.augment;

/**
 * Created by Matthew on 12/11/2014.
 */
public class Models {
    public static class ProcessMemoryUsage {
        public String mProcessName;
        public int mProcessId;

        public int mDalvikPrivate;
        public int mDalvikShared;
        public int mDalvikPss;

        public int mNativePrivate;
        public int mNativeShared;
        public int mNativePss;

        public int mOtherPrivate;
        public int mOtherShared;
        public int mOtherPss;

        public int mTotalPrivate;
        public int mTotalShared;
        public int mTotalPss;

        public ProcessMemoryUsage(String processName, int processId,
                                  int dalvikPrivate, int dalvikShared, int dalvikPss,
                                  int nativePrivate, int nativeShared, int nativePss,
                                  int otherPrivate, int otherShared, int otherPss,
                                  int totalPrivate, int totalShared, int totalPss){
            mProcessName = processName;
            mProcessId = processId;
            mDalvikPrivate = dalvikPrivate;
            mDalvikShared = dalvikShared;
            mDalvikPss = dalvikPss;
            mNativePrivate = nativePrivate;
            mNativeShared = nativeShared;
            mNativePss = nativePss;
            mOtherPrivate = otherPrivate;
            mOtherShared = otherShared;
            mOtherPss = otherPss;
            mTotalPrivate = totalPrivate;
            mTotalShared = totalShared;
            mTotalPss = totalPss;
        }
    }

    public static class ProcessCPUUsage {
        String mProcessName;
        String mProcessId;
        String mCpuUsage; //Starting off as line;

        public ProcessCPUUsage(String processName, String processId, String cpuUsage){
            mProcessName = processName;
            mProcessId = processId;
            mCpuUsage = cpuUsage;
        }
    }
}
