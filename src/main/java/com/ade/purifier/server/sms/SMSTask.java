package com.ade.purifier.server.sms;

/**
 *
 * Created by ismeade on 2014/12/31.
 */
public class SMSTask {

    private long   createTime;
    private long   endTime;
    private String code;
    private String mobile;

    SMSTask(String code, String mobile, int timeout) {
        this.createTime = System.currentTimeMillis();
        this.endTime = createTime + (long) (timeout * 1000);
        this.code = code;
        this.mobile = mobile;
    }

    public boolean isTimeout() {
        return System.currentTimeMillis() > endTime;
    }

    public long getCreateTime() {
        return createTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public static void main(String[] args) throws Exception {
        SMSTask s = new SMSTask("111111", "1230000000", 1);
        Thread.sleep(2000);
        System.out.println(s.getCreateTime());
        System.out.println(s.getEndTime());
        System.out.println(s.getEndTime() - s.getCreateTime());
        System.out.println(s.isTimeout());
    }

}
