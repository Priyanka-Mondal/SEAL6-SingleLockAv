package com.example.singlelockav;


import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends Activity {
	
	Queue<Integer> q = new LinkedList<Integer>();
	 Lock lock= new ReentrantLock();
	 final Handler mhand = new  Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        final Handler mainHandler = new  Handler(Looper.getMainLooper());
		Thread t = new Thread()
		{
			public void run()
			{
				Runnable myRunnable = new Runnable()
				{
					public void run()
					{
						
						lock.lock();
						System.out.println("---------after lock in t2--------------"+ Thread.currentThread().getName());
						q.clear();
						lock.unlock();
						System.out.println("---------after unlock in t2--------------");
					};
				};
				mainHandler.post(myRunnable);	
			};
		};
		t.start();
		s();
		A();
	
    }
    
    public void A()
	{
		
		lock.lock();
		System.out.println("---------after lockkk--------------");
		if(!q.isEmpty())
		{
			Runnable myRunnable = new Runnable()
			{
				public void run()
				{
					q.remove();					
					lock.unlock();
					System.out.println("---------after unlock--------------");
				};
			};
			mhand.post(myRunnable);	
		}
		else
		{
			lock.unlock();
			System.out.println("---------after unlock2--------------");
		}
		
		
		System.out.println("---------End of A------------");
		
	}
    
    public void s()
	{
		q.add(55);
	}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
