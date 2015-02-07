package net.shenru.xixi.iflytek;

import java.util.ArrayList;

import net.shenru.xixi.BaseActivity;
import net.shenru.xixi.R;
import net.shenru.xixi.pocket.XixiService;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Toast;

import com.iflytek.speech.RecognizerResult;
import com.iflytek.speech.SpeechConfig.RATE;
import com.iflytek.speech.SpeechError;
import com.iflytek.ui.RecognizerDialog;
import com.iflytek.ui.RecognizerDialogListener;

/**
 * @author along IAT ( iFly Auto Transform ) 语音听写(iFly Auto
 *         Transform,IAT)是一种使计算机能够识别人通过麦克风或者电话输入的词语
 *         或语句的技术,简单的说就是将语音中的具体内容转换成文字,更适合于日常用语的识别。
 */
public class IatActivity extends BaseActivity implements
		RecognizerDialogListener {
	private RecognizerDialog mRecognizerDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Toast.makeText(getApplicationContext(), "IatActivity start", 0).show();

		// Window window = getWindow();
		// WindowManager.LayoutParams wl = window.getAttributes();
		// wl.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		// wl.alpha = 0.0f;
		// // window.setAttributes(wl);
		// TextView tv = new TextView(this);
		// tv.setBackgroundColor(Color.TRANSPARENT);
		// tv.setLayoutParams(new
		// LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
		// ViewGroup.LayoutParams.FILL_PARENT));
		// setContentView(tv);
		// getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);

		setContentView(R.layout.translucent_background);

		XixiService.getInstace().stopLook(this.getClass().getSimpleName());
		SystemClock.sleep(1000);
		mRecognizerDialog = new RecognizerDialog(this, "appid="
				+ getString(R.string.iflytek_key));
		mRecognizerDialog.setListener(this);
		// 参数设置
		mRecognizerDialog.setEngine("sms", null, null);
		// 设置采样率
		mRecognizerDialog.setSampleRate(RATE.rate8k);
		// 显示
		//mRecognizerDialog.show();
		mRecognizerDialog.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
//				Intent intent = new Intent("com.shenru.command");
//				intent.putExtra("data", "shell:lat finish");
//				sendBroadcast(intent);
				finish();
			}
		});

		getWindow().getDecorView().getViewTreeObserver()
				.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						if (!mRecognizerDialog.isShowing()) {
							mRecognizerDialog.show();
						}
					}
				});
	}

	// 回调
	@Override
	public void onEnd(SpeechError error) {
		if (error != null) {
			if (mRecognizerDialog.isShowing()) {
				mRecognizerDialog.dismiss();
			}
			Intent intent = new Intent("com.shenru.command");
			intent.putExtra("error", error.getErrorCode());
			sendBroadcast(intent);
		} else {

		}
		// 10118 你好想没有说话
		// 20001 没有检测到网络
	}

	@Override
	public void onResults(ArrayList<RecognizerResult> results, boolean isLast) {
		StringBuilder builder = new StringBuilder();
		for (RecognizerResult recognizerResult : results) {
			builder.append(recognizerResult.text);
		}

		Intent intent = new Intent("com.shenru.command");
		intent.putExtra("execute", builder.toString());
		sendBroadcast(intent);
	}

}
