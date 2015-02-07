package net.shenru.xixi.pocket;

import net.shenru.xixi.ActionListener;
import net.shenru.xixi.iflytek.IatActivity;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import edu.cmu.pocketsphinx.demo.RecognitionListener;
import edu.cmu.pocketsphinx.demo.RecognizerTask;

public class PocketSphinxService extends Service implements RecognitionListener, ActionListener {
	static {
		System.loadLibrary("pocketsphinx_jni");
	}
	private static final String TAG = PocketSphinxService.class.getSimpleName();

	// 任务
	RecognizerTask rec;
	// 线程
	Thread rec_thread;
	// 标志位
	private boolean mListening = false;
	private boolean mIsCallback = false;
	private long mStartTime = 0;
	private XixiService mXixiService;

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "onCreate");
		Toast.makeText(this, "我是西西", 0).show();

		mXixiService = XixiService.getInstace();
		mXixiService.registerActionListener(this);
		rec = new RecognizerTask();
		rec.setRecognitionListener(this);
		rec_thread = new Thread(this.rec);
		rec_thread.start();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (rec != null && mListening) {
			rec.stop();
		}
	}

	public void stop() {
		if (mListening) {
			mListening = false;
			rec.stop();
		}
	}

	public void start() {
		mStartTime = System.currentTimeMillis();
		if (!mListening) {
			mListening = true;
			mIsCallback = false;
			rec.start();
		}
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// RecognitionListener
	@Override
	public void onPartialResults(Bundle b) {
		long current = System.currentTimeMillis();
		if (current-  mStartTime > 1000) {
			if (!mIsCallback) {
				mIsCallback = true;
				Intent intent = new Intent(this, IatActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
		}
	}

	@Override
	public void onError(int err) {
		// TODO Auto-generated method stub
		Log.i(TAG, "err:" + err);
	}

	@Override
	public void startLook(String clazz) {
		start();
	}

	@Override
	public void stopLook(String clazz) {
		stop();
	}

	@Override
	public void reply(String clazz) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResults(Bundle b) {
		// TODO Auto-generated method stub

	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
