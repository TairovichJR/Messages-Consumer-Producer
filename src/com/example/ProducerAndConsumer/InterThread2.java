package com.example.ProducerAndConsumer;
;
import java.util.ArrayList;
import java.util.List;

public class InterThread2 {
    public static void main(String[] args) {

        List<Integer> sharedList = new ArrayList<>();
        Thread t1 = new Thread(new Producer1(sharedList));
        Thread t2 = new Thread(new Consumer1(sharedList));
        t1.setName("Producer Thread");
        t2.setName("Consumer Thread");

        t1.start();
        t2.start();

    }
}

class Producer1 implements Runnable{
    private List<Integer> sharedList;
    private final int MAX_COUNT = 5;
    private int i = 0;

    Producer1(List<Integer> sharedList){
        this.sharedList = sharedList;
    }

    public void run(){
        while (true){
            try {
                produce(i++);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void produce(int i) throws InterruptedException{

        synchronized (sharedList){
            while (sharedList.size() == MAX_COUNT){
                System.out.println(ThreadColor.ANSI_PURPLE +"sharedList is full at "+ MAX_COUNT +"...waiting for consumer to consume elements");
                sharedList.wait();
            }
        }
        synchronized (sharedList){
            System.out.println(ThreadColor.ANSI_GREEN +Thread.currentThread().getName() + " is producing item " + i);
            sharedList.add(i);
            Thread.sleep(5);
            sharedList.notifyAll();
        }
    }
}

class Consumer1 implements Runnable{
    private List<Integer> sharedList;

    public Consumer1(List<Integer> sharedList){
        this.sharedList = sharedList;
    }

    public void run(){
        while (true){
            try{
                consume();
            }catch (InterruptedException e){

            }
        }
    }

    public void consume() throws InterruptedException{

        synchronized (sharedList){
            while (sharedList.isEmpty()){
                System.out.println(ThreadColor.ANSI_PURPLE +"sharedList is empty at " +sharedList.size()+".... waiting for producer to produce items");
                sharedList.wait();
            }
        }
        synchronized (sharedList){
            Thread.sleep(15);
            System.out.println(ThreadColor.ANSI_RED + Thread.currentThread().getName() +" is consuming item " + sharedList.remove(0));
            sharedList.notifyAll();
        }
    }
}



















































