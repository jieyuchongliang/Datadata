package vlayout.fujisoft.com.datadata;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button read, write;
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        read = (Button) findViewById(R.id.read);
        write = (Button) findViewById(R.id.write);
        read.setOnClickListener(this);
        write.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.read:
                read();
                break;
            case R.id.write:
                write();
                break;
        }
    }

    private void write() {
        File filesDir = getFilesDir();//获取data/data目录下的路径
        Log.i(TAG, "项目私有路径file: " + filesDir);
        String priPath = filesDir.getPath();
        Log.i(TAG, "项目私有路径path: " + priPath);
        File dummyDataFile = new File("/data/data/vlayout.fujisoft.com.datadata");//目标文件夹（写入）
        if (!dummyDataFile.exists()) {
            dummyDataFile.mkdirs();
        }

        File dir = Environment.getExternalStorageDirectory();
        String path = dir.getAbsolutePath() + "/dummydata";
        File dummydata = new File(path);//目标文件夹（读取）
        copy(new File[]{dummydata},dummyDataFile);
        fileDetail(dummydata);//文件夹详情
    }

    /**
     * 即将读取的文件夹的详细信息
     * @param dummydata
     */
    private void fileDetail(File dummydata) {
        int fileNum = 0, folderNum = 0;
        if (dummydata.exists()) {
            LinkedList<File> list = new LinkedList<File>();
            File[] files = dummydata.listFiles();
            for (File file2 : files) {
                if (file2.isDirectory()) {
                    Log.i(TAG, "---log输出:文件夹+" + file2.getAbsolutePath());
                    list.add(file2);
                    folderNum++;
                } else {
                    Log.i(TAG, "---log输出:文件+" + file2.getAbsolutePath());
                    fileNum++;
                }
            }
            File temp_file;
            while (!list.isEmpty()) {
                temp_file = list.removeFirst();
                files = temp_file.listFiles();
                for (File file2 : files) {
                    if (file2.isDirectory()) {
                        Log.i(TAG, "---log输出:文件夹+" + file2.getAbsolutePath());
                        list.add(file2);
                        folderNum++;
                    } else {
                        Log.i(TAG, "---log输出:文件+" + file2.getAbsolutePath());
                        fileNum++;
                    }
                }
            }
        } else {
            Log.i(TAG, "---log输出:文件不存在! ");
        }
    }

    /**
     * 读取按钮的点击事件
     */
    private void read() {
        File dummyDataFile = new File("/data/data/vlayout.fujisoft.com.datadata/dummydata");//目标文件夹（写入）
        if (!dummyDataFile.exists()) {
            dummyDataFile.mkdirs();
        }
        fileDetail(dummyDataFile);
    }

    /**
     * 复制文件夹
     * @param fl
     * @param file
     */
    public void copy(File[] fl, File file) {
        if (!file.exists()) // 如果文件夹不存在
            file.mkdir(); // 建立新的文件夹
        for (int i = 0; i < fl.length; i++) {
            if (fl[i].isFile()) { // 如果是文件类型就复制文件
                try {
                    FileInputStream fis = new FileInputStream(fl[i]);
                    FileOutputStream out = new FileOutputStream(new File(file
                            .getPath()
                            + File.separator + fl[i].getName()));

                    int count = fis.available();
                    byte[] data = new byte[count];
                    if ((fis.read(data)) != -1) {
                        out.write(data); // 复制文件内容
                    }
                    out.close(); // 关闭输出流
                    fis.close(); // 关闭输入流
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (fl[i].isDirectory()) { // 如果是文件夹类型
                File des = new File(file.getPath() + File.separator
                        + fl[i].getName());
                des.mkdir(); // 在目标文件夹中创建相同的文件夹
                copy(fl[i].listFiles(), des); // 递归调用方法本身
            }
        }
    }
}
