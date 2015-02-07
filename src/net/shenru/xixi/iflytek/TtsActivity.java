package net.shenru.xixi.iflytek;

import net.shenru.xixi.BaseActivity;
import net.shenru.xixi.R;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.iflytek.speech.SpeechError;
import com.iflytek.speech.SynthesizerPlayer;
import com.iflytek.speech.SynthesizerPlayerListener;

public class TtsActivity extends BaseActivity implements SynthesizerPlayerListener {

	private static final String TAG = TtsActivity.class.getSimpleName();
	private SynthesizerPlayer mSynthesizerPlayer;
	private Toast mToast;
	
	//缓冲进度
	private int mPercentForBuffering = 0;
	
	//播放进度
	private int mPercentForPlaying = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mToast = Toast.makeText(this, "", 0);
		player();
	}
	public void onStop(){
		super.onStop();
		mToast.cancel();
		if(null == mSynthesizerPlayer){
			mSynthesizerPlayer.cancel();
		}
	}
	private void player() {
		// TODO Auto-generated method stub
		mSynthesizerPlayer = SynthesizerPlayer.createSynthesizerPlayer(this, "appid=" + getString(R.string.iflytek_key));
		mSynthesizerPlayer.setVoiceName("vixq");
		mSynthesizerPlayer.setSpeed(50);
		mSynthesizerPlayer.setVolume(50);
		mSynthesizerPlayer.playText("我的名字叫西西", null, this);
	}
	@Override
	public void onBufferPercent(int percent,int beginPos,int endPos) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onBufferPercent percent:" + percent +",beginPos:" + beginPos +",endPos:" + endPos);
		mPercentForBuffering = percent;
		mToast.setText(String.format("当前合成进度为%d%%，播放进度为%d%%",mPercentForBuffering, mPercentForPlaying));
		mToast.show();
	}
	@Override
	public void onEnd(SpeechError error) {
		// TODO Auto-generated method stub
		if(error != null){
			Log.i(TAG, "onEnd SpeechError:"  + error.getErrorCode());
		}else{
			mToast.setText("播放完毕");
			mToast.show();
		}
		
	}
	@Override
	public void onPlayBegin() {
		// TODO Auto-generated method stub
		Log.i(TAG, "onPlayBegin");
	}
	@Override
	public void onPlayPaused() {
		// TODO Auto-generated method stub
		Log.i(TAG, "onPlayPaused");
	}
	@Override
	public void onPlayPercent(int percent,int beginPos,int endPos) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onPlayPercent percent:" + percent +",beginPos:" + beginPos +",endPos:" + endPos);
		mPercentForPlaying = percent;
		mToast.setText(String.format("当前合成进度为%d%%，播放进度为%d%%",mPercentForBuffering, mPercentForPlaying));
		mToast.show();
	}
	@Override
	public void onPlayResumed() {
		// TODO Auto-generated method stub
		Log.i(TAG, "onPlayResumed");
	}
}
