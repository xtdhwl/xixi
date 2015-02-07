package net.shenru.xixi.app;

import java.io.File;
import java.util.Collections;
import java.util.LinkedList;

import net.shenru.xixi.R;
import android.content.Context;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Environment;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class PlayMusicer implements OnClickListener {

	private Context mContext;
	private View mView;
	MediaPlayer mPlayer = null;
	WindowManager mWindowManager = null;
	private boolean isShow = false;;

	private LinkedList<String> mFiles;
	private int mIndex = 0;
	private View mPreviousView;
	private Button mPlayView;
	private View mNextView;
	private View mQuitView;
	private TextView mNameVew;

	public PlayMusicer(Context context) {
		super();
		this.mContext = context;

		mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);

		mView = View.inflate(mContext, R.layout.play_music_fragment, null);
		mPreviousView = mView.findViewById(R.id.previous);
		mPlayView = (Button) mView.findViewById(R.id.play);
		mNextView = mView.findViewById(R.id.next);
		mQuitView = mView.findViewById(R.id.quit);
		mNameVew = (TextView) mView.findViewById(R.id.name);

		mPreviousView.setOnClickListener(this);
		mPlayView.setOnClickListener(this);
		mNextView.setOnClickListener(this);
		mQuitView.setOnClickListener(this);

		mFiles = new LinkedList<String>();
		String sdPath = Environment.getExternalStorageDirectory().getPath();
		File file = new File(sdPath + "/shenru/music");
		if (file.exists() && file.isDirectory()) {
			Collections.addAll(mFiles, file.list(null));
		}
		mIndex = 0;
		refreshView();
	}

	private void refreshView() {
		if (mIndex <= 0) {
			mPreviousView.setEnabled(false);
		} else {
			mPreviousView.setEnabled(true);
		}

		if (mIndex >= mFiles.size() - 1) {
			mNextView.setEnabled(false);
		} else {
			mNextView.setEnabled(true);
		}
	}

	private void play() {
		mPlayView.setText("停止");
		try {
			if (mIndex < mFiles.size()) {
				String file = mFiles.get(mIndex);
				if (mPlayer == null) {
					mPlayer = new MediaPlayer();
					mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
					mPlayer.setOnCompletionListener(new OnCompletionListener() {
						@Override
						public void onCompletion(MediaPlayer mp) {
							if (mNextView.isEnabled()) {
								next();
							}
						}
					});
					mPlayer.setDataSource("/sdcard/shenru/music/" + file);
					mPlayer.prepare();
					mPlayer.start();
					mNameVew.setText(file);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void stop() {
		if (mPlayer != null) {
			if (mPlayer.isPlaying()) {
				mPlayer.stop();
			}
			mPlayer.release();
			mPlayer = null;
		}
		mPlayView.setText("播放");
	}

	private void previous() {
		mIndex--;
		refreshView();
		stop();
		play();
	}

	private void next() {
		mIndex++;
		refreshView();
		stop();
		play();
	}

	public void showView() {
		if (isShow) {
			return;
		}
		isShow = true;

		WindowManager.LayoutParams p = new WindowManager.LayoutParams();
		p.gravity = Gravity.CENTER;
		p.width = -1;
		p.height = -2;

		p.format = PixelFormat.RGBA_8888;
		p.type = WindowManager.LayoutParams.TYPE_PHONE;
		p.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
				| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

		// p.format = PixelFormat.TRANSLUCENT;
		// p.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
		// p.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT |
		// WindowManager.LayoutParams.TYPE_TOAST;
		p.setTitle("PopupWindow:");

		mWindowManager.addView(mView, p);

		play();
	}

	public void release() {
		if (mView != null) {
			mWindowManager.removeView(mView);
			mView = null;
		}
		isShow = false;
		if (mPlayer != null) {
			mPlayer.release();
			mPlayer = null;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.previous:
			previous();
			break;
		case R.id.play:
			String str = mPlayView.getText().toString();
			if("播放".equals(str)){
				play();
			}else{
				stop();
			}
			
			break;
		case R.id.next:
			next();
			break;
		case R.id.quit:
			release();
			break;
		}
	}

}
