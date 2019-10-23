package thread;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


class Phone implements Runnable{
    public synchronized void sendSMS() throws Exception {
        System.out.println(Thread.currentThread().getName() + "\t ---invoked sendSMS()---");
        sendEmail();
    }

    public synchronized void sendEmail() throws Exception {
        System.out.println(Thread.currentThread().getName() + "\t ---invoked sendEmail()---");
    }


    Lock lock = new ReentrantLock();
    @Override
    public void run() {
        get();
    }


    public void get() {
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + "\t ---invoked get()---");
            set();
        } finally {
            lock.unlock();
        }
    }

    public void set() {
        lock.lock();
        try {
            System.out.println(Thread.currentThread().getName() + "\t ---invoked set()---");
        } finally {
            lock.unlock();
        }
    }

}

public class ReentrantLockDemo {
    public static void main(String[] args) {
        Phone phone = new Phone();
        new Thread(() -> {
            try {
                phone.sendSMS();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "t1").start();


        new Thread(() -> {
            try {
                phone.sendSMS();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "t2").start();


        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("------------------------------");

        new Thread(phone, "t3").start();
        new Thread(phone, "t4").start();

/*
        Output:

        t1	 ---invoked sendSMS()---
        t1	 ---invoked sendEmail()---
        t2	 ---invoked sendSMS()---
        t2	 ---invoked sendEmail()---
        ------------------------------
        t3	 ---invoked get()---
        t3	 ---invoked set()---
        t4	 ---invoked get()---
        t4	 ---invoked set()---
*/

    }
}
