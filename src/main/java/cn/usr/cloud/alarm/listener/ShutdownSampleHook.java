package cn.usr.cloud.alarm.listener;

/**
 * @Auther: Andy(李永强)
 * @Date: 2019/6/13 上午10:20
 * @Describe: 这个暂时没用上，初衷是用于在结束进程的时候确保定时保存报警历史记录的逻辑不会出现数据丢失。
 */
public class ShutdownSampleHook extends Thread {
    private Thread mainThread;
    @Override
    public void run() {
        System.out.println("2: Shut down signal received.");
        AlarmRecordHandler.runing = false;
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mainThread.interrupt();//给主线程发送一个中断信号
        try {
            mainThread.join(); //等待 mainThread 正常运行完毕
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("5: Shut down complete.");
    }

    public ShutdownSampleHook(Thread mainThread) {
        this.mainThread=mainThread;

    }
}
