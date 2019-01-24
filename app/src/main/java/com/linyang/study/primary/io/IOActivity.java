package com.linyang.study.primary.io;

import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.linyang.study.R;
import com.linyang.study.app.util.LogUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 描述:
 * Created by fzJiang on 2019/01/02 9:02 星期三
 */
public class IOActivity extends AppCompatActivity {

    private static final String TEST_FILE = Environment.getExternalStorageDirectory().getAbsolutePath() + "/UserWords.txt";
    private static final String TEST_FILE_OUT = Environment.getExternalStorageDirectory().getAbsolutePath() + "/UserWords_1.txt";
    private static final String TEST_FILE_OUT_2 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/UserWords_2.txt";
    private static final String TEST_FILE_OUT_3 = Environment.getExternalStorageDirectory().getAbsolutePath() + "/UserWords_3.txt";

    @BindView(R.id.bt_byte_read_write)
    AppCompatButton btByteReadWrite;
    @BindView(R.id.bt_char_read_write)
    AppCompatButton btCharReadWrite;
    @BindView(R.id.bt_buffer_read_write)
    AppCompatButton btBufferReadWrite;
    @BindView(R.id.bt_byte_to_buffer)
    AppCompatButton btByteToBuffer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_io);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.bt_byte_read_write, R.id.bt_char_read_write, R.id.bt_buffer_read_write, R.id.bt_byte_to_buffer})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bt_byte_read_write:
                readWriteFileByByte();
                break;

            case R.id.bt_char_read_write:
                readWriteFileByCharacter();
                break;

            case R.id.bt_buffer_read_write:
                readWriteFileByBuffer();
                break;

            case R.id.bt_byte_to_buffer:
                byte2buffer();
                break;
        }
    }

    private void readWriteFileByByte() {
        long time = System.currentTimeMillis();
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(new File(TEST_FILE));
            outputStream = new FileOutputStream(new File(TEST_FILE_OUT));
            byte[] read = new byte[1024];
            while (inputStream.read(read) != -1) {
                outputStream.write(read);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null && outputStream != null) {
                try {
                    inputStream.close();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        LogUtil.i("----------字节流--读--结束--耗时:" + (System.currentTimeMillis() - time));
    }

    private void readWriteFileByCharacter() {
        long time = System.currentTimeMillis();
        FileReader fileReader = null;
        FileWriter fileWriter = null;
        try {
            fileReader = new FileReader(TEST_FILE);
            fileWriter = new FileWriter(TEST_FILE_OUT_2);
            char[] read = new char[1024];
            while (fileReader.read(read) != -1) {
                fileWriter.write(read);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileReader != null && fileWriter != null) {
                try {
                    fileReader.close();
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        LogUtil.i("----------字符流--读--结束--耗时:" + (System.currentTimeMillis() - time));
    }

    private void readWriteFileByBuffer() {
        long time = System.currentTimeMillis();
        BufferedReader bufferedReader = null;
        BufferedWriter bufferedWriter = null;
        try {
            bufferedReader = new BufferedReader(new FileReader(TEST_FILE));
            bufferedWriter = new BufferedWriter(new FileWriter(TEST_FILE_OUT_3));
            String read;
            while ((read = bufferedReader.readLine()) != null) {
                bufferedWriter.write(read);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null && bufferedWriter != null) {
                try {
                    bufferedReader.close();
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        LogUtil.i("----------缓冲流--读--结束--耗时:" + (System.currentTimeMillis() - time));
    }

    private void byte2buffer() {
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;
        try {
            URL url = new URL("https://www.jianshu.com/p/509c78602ed2");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            inputStream = urlConnection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder stringBuilder = new StringBuilder();
            String temp;
            while ((temp = bufferedReader.readLine()) != null) {
                stringBuilder.append(temp);
            }
            LogUtil.i(stringBuilder.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null && bufferedReader != null) {
                try {
                    inputStream.close();
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
