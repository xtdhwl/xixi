package net.shenru.xixi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.shenru.xixi.app.CommandService;
import net.shenru.xixi.iflytek.IatActivity;
import net.shenru.xixi.iflytek.TtsActivity;
import net.shenru.xixi.pocket.PocketSphinxService;
import net.shenru.xixi.pocket.XixiService;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends BaseActivity {

	private static final String TAG = MainActivity.class.getSimpleName();

	private static final int Iat_request_code = 100;
	private TextView mLogView;
	private Button mServiceView;

	private boolean isReply = false;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			SystemClock.sleep(500);
			Intent it = new Intent(getActivity(), IatActivity.class);
			it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivityForResult(it, Iat_request_code);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findView();

		Intent it = new Intent(getActivity(), PocketSphinxService.class);
		startService(it);
		Intent commandService = new Intent(getActivity(), CommandService.class);
		startService(commandService);
		
		
		Intent intent = new Intent("com.shenru.command");
		intent.putExtra("execute", "打开播放器");
		sendBroadcast(intent);
	}

	private void startPocketService() {
		Intent it = new Intent(this, PocketSphinxService.class);
		startService(it);
		printStr("启动后台服务.");
	}

	private void startIatActivity() {
		Intent it = new Intent(this, IatActivity.class);
		startActivity(it);
		printStr("启动语音识别.");
	}

	private void startTtsActivity() {
		Intent it = new Intent(this, TtsActivity.class);
		startActivity(it);
		printStr("启动语音合成.");
	}

	private void findView() {
		mLogView = (TextView) findViewById(R.id.txt_log);
		findViewById(R.id.btn_lat).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent it = new Intent(getActivity(), IatActivity.class);
				startActivity(it);
			}
		});

		findViewById(R.id.btn_start).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Button btv = (Button) v;
				String text = btv.getText().toString();
				if (text.equals("启动服务")) {
					btv.setText("停止服务");
					printStr("启动服务");
					XixiService.getInstace().startLook(TAG);
				} else {
					btv.setText("启动服务");
					XixiService.getInstace().stopLook(TAG);
					printStr("停止服务");
				}
			}
		});

		findViewById(R.id.btn_pocket).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {

					String sdPath = Environment.getExternalStorageDirectory()
							.getPath();
					File file = new File(sdPath + "/data/test");
					if (file.exists()) {
						file.delete();
					}
					copyPocketSphinxFileToSD("poketSphinx/hmm/tdt_sc_8k",
							sdPath + "/data/test/hmm/tdt_sc_8k");
					copyPocketSphinxFileToSD("poketSphinx/lm", sdPath
							+ "/data/test/lm");

					// startPocketService();
					Toast.makeText(getApplicationContext(), "复制完成", 0).show();
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(getApplicationContext(), "复制失败", 0).show();
				}
			}
		});
	}

	private void printStr(String str) {
		mLogView.setText(mLogView.getText() + "\n" + str);
	}

	private boolean copyPocketSphinxFileToSD(String dest, String src) {

		try {

			File file = new File(src);
			if (!file.exists()) {
				file.mkdirs();
			}

			AssetManager assets = getAssets();
			String[] list = assets.list(dest);
			for (String path : list) {
				Log.i(TAG, "path:" + path);
				copy(dest + "/", path, src + "/");
			}

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	private void copy(String prefix, String file, String src) {

		AssetManager assets = getAssets();

		InputStream ins = null;
		OutputStream ops = null;

		try {
			ins = assets.open(prefix + file);
			ops = new FileOutputStream(src + file);

			byte[] buf = new byte[1024];
			int len = -1;
			while ((len = ins.read(buf)) != -1) {
				ops.write(buf, 0, len);
			}

			ins.close();
			ops.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ins != null) {
				try {
					ins.close();
				} catch (IOException e) {
					// e.printStackTrace();
				}
			}
			if (ops != null) {
				try {
					ops.close();
				} catch (IOException e) {
					// e.printStackTrace();
				}
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	// //////////////////////////////////////////////////////////////////////////////////////////////////

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		isReply = false;
		if (requestCode == Iat_request_code) {
			if (resultCode == RESULT_OK) {
				Toast.makeText(this, "commend:" + data.getStringExtra("data"),
						0).show();
			} else if (resultCode == -2) {
				Toast.makeText(this,
						"error:" + data.getIntExtra("error", -99999999), 0)
						.show();
			} else {
				Toast.makeText(this, "canceled", 0).show();
			}
			SystemClock.sleep(500);
		}
	}
}
