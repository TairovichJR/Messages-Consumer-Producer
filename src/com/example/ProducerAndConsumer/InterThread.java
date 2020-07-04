package com.example.ProducerAndConsumer;

public class InterThread {

    public static void main(String[] args) {
        Q q = new Q();
        new Producer(q);
        new Consumer(q);
    }
}

class Q{
    int num;
    boolean setValue = false;

    public synchronized void put(int num){
        while (setValue){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Put: " + num);
        this.num = num;
        setValue = true;
        notify();
    }
    public synchronized int get(){
        while (!setValue){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Get: " + num);
        setValue = false;
        notify();
        return num;
    }
}

class Producer implements Runnable{
    private Q q;
    public Producer(Q q){
        this.q = q;
        Thread t = new Thread(this,"Producer");
        t.start();
    }
    public void run(){
        int i = 0;
        while (true){
            q.put(i++);
            try{
                Thread.sleep(1000);
            }catch (InterruptedException e){}
        }
    }
}

class Consumer implements Runnable{
    private Q q;
    public Consumer(Q q){
        this.q = q;
        Thread t = new Thread(this,"Consumer");
        t.start();
    }

    public void run(){
        int i = 0;
        while (true){
            q.get();
            try{
                Thread.sleep(1000);
            }catch (InterruptedException e){}
        }
    }
}