package test.server;

public class Response extends Request {

    private long spendTime;

    private String resultCode;

    private String resultMsg;

    public long getSpendTime() {
        return spendTime;
    }

    public void setSpendTime(long spendTime) {
        this.spendTime = spendTime;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public static Response copyRequest(Request resp){
        Response req = new Response();
        req.setCommand(resp.getCommand());
        req.setSequence(resp.getSequence());
        req.setSessionId(resp.getSessionId());
        req.setData(resp.getData());
        return req;
    }
}
