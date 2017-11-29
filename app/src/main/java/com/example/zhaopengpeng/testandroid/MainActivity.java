package com.example.zhaopengpeng.testandroid;


import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhaopengpeng.testandroid.bean.ResponseBean;
import com.example.zhaopengpeng.testandroid.bean.WordBean;
import com.example.zhaopengpeng.testandroid.http.HttpUtils;
import com.example.zhaopengpeng.testandroid.view.SelectableTextView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SelectableTextView.SelectableTextListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private SelectableTextView tvHello;
    private TextView tvWord;
    private TextView tvPronunciation;
    private TextView tvDescribe;
    private Button buttonPlay;
    private Button buttonImageList;
    private BottomSheetBehavior behavior;

    private String mAudioUrl;
    private String mNewAudioUrl;
    private ProgressDialog mProgressDialog;
    private Disposable mDisposable;
    private MediaPlayer mPlayer;

    private String content = "KIEV — The European Union warned Ukraine on Thursday time was running out to revive shelved deals on free trade and political association by meeting the bloc's concerns over the jailing of opposition leader Yulia Tymoshenko and bringing in reforms.\n" +
            "A senior EU official also made it clear the agreements would fall through if Ukraine\n" +
            "    \n" +
            " joined the Russia-led post-Soviet Customs Union trade bloc. \"We have a window of opportunity. But time is short,'' Stefan Fuele, the European Commissioner for Enlargement and European Neighbourhood Policy, said on a visit to Ukraine.\n" +
            "Brussels put off signing the landmark agreements after a Ukrainian court jailed former prime minister Tymoshenko, President Viktor Yanukovich's main opponent, on an abuse- of-office charge in October 2011.\n" +
            "The EU says the Tymoshenko case and those of other prosecuted opposition politicians are examples of selective justice and are a barrier to Ukraine's ambition of European integration.\n" +
            "Two other issues raised by the bloc are related to the electoral system, which came under fire from Western observers following the parliamentary election in October, and legal reforms needed to bring Ukraine closer to EU standards.\n" +
            "\"The European Union is committed to signing the association agreement...provided there is determined action and tangible progress on the three key issues: selective justice, addressing the shortcomings of the October election and advancing the association agenda reforms,'' Fuele told reporters. \"After several recent setbacks in Ukraine there is a need to regain confidence that Ukraine could emerge as a modern European country.''\n" +
            "Fuele, whose visit may set the tone of a Feb. 25 EU-Ukraine summit, said the two agreements could be signed at the EU's Eastern Partnership summit in November if the former Soviet republic met the bloc's conditions.\n" +
            "But he warned the Kiev government that joining a customs union with Russia, aggressively promoted by Moscow, would ruin those prospects.\n" +
            "\"Joining any structure which would imply transferring the ability to set tariffs and define its trade policy to a supranational body would mean that Ukraine would no longer be able to implement the tariff dismantling agreed with the European Union in the context of the DCFTA [Deep and Comprehensive Free Trade Agreement],'' Fuele said in a speech at the Ukrainian parliament.\n" +
            "\"It would also not be able anymore to regulate areas such as food standards, or technical product standards, all of them important in the framework of the DCFTA. It will not be able to integrate economically with the European Union,\" he continued.\n" +
            "Ukrainian officials say they are committed to European integration. But they say they are also looking for a way to cooperate with the Customs Union because both blocs are Ukraine's major trade partners.\n" +
            "Fuele urged Ukraine to make sure it adopts and implements laws that actually work and adhere to European standards, citing as an example the law on state procurement - purchases of goods and services by the government.\n" +
            "The EU suspended some of its Ukraine financial aid programs after Kiev adopted a law on state procurement which Brussels said was riddled with loopholes and thus failed to ensure transparent and competitive procedures.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvHello = $(R.id.tv_hello);
        tvWord = $(R.id.tv_word);
        tvPronunciation = $(R.id.tv_pronunciation);
        tvDescribe = $(R.id.tv_describe);
        buttonPlay = $(R.id.button_play);
        buttonImageList = $(R.id.button_image_list);
        View bottomSheetView = $(R.id.bottom_sheet);
        behavior = BottomSheetBehavior.from(bottomSheetView);
        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        tvHello.setContent(content);
        setListener();
        bottomSheetView.setOnClickListener(this);
        setTitle("English Reading");
    }

    private void setListener() {
        buttonPlay.setOnClickListener(this);
        buttonImageList.setOnClickListener(this);
        tvHello.setOnSelectableTextListener(this);
    }

    /**
     * 调用网络接口
     *
     * @param word
     */
    private void requestData(final String word) {

        mDisposable = HttpUtils.getApiService().getWordDetail(word)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        showWaitingDialog();
                    }
                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        dismissProgressDialog();
                    }
                })
                .filter(new Predicate<ResponseBean<WordBean>>() {
                    @Override
                    public boolean test(@NonNull ResponseBean<WordBean> wordBeanResponseBean) throws Exception {
                        return wordBeanResponseBean != null && wordBeanResponseBean.getData() != null;
                    }
                })
                .subscribeWith(new DisposableObserver<ResponseBean<WordBean>>() {
                    @Override
                    public void onNext(ResponseBean<WordBean> bean) {
                        WordBean wordBean = bean.getData();
                        if (wordBean == null) return;
                        tvWord.setText(word);
                        tvPronunciation.setText(wordBean.getPronunciation());
                        tvDescribe.setText(wordBean.getDefinition());
                        mNewAudioUrl = wordBean.getAudio();
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    private void showBehavior() {
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    private <T extends View> T $(int id) {
        return (T) findViewById(id);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_play:
                toPlay();
                break;

            case R.id.button_image_list:
                ImageListActivity.startActivity(this);
                break;
        }
    }

    private void toPlay() {
        new PlayerThread().start();
    }

    private class PlayerThread extends Thread {
        @Override
        public void run() {
            if (TextUtils.isEmpty(mNewAudioUrl)) return;
            if (!mNewAudioUrl.equals(mAudioUrl)) {
                if (mPlayer != null)
                    mPlayer.release();
                mAudioUrl = mNewAudioUrl;
                Uri uri = Uri.parse(mAudioUrl);
                mPlayer = MediaPlayer.create(MainActivity.this, uri);
            }
            mPlayer.start();
        }
    }


    public void showWaitingDialog() {
        dismissProgressDialog();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getString(R.string.loading_msg));
        mProgressDialog.show();
    }

    public void dismissProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        mProgressDialog = null;
    }

    @Override
    public void onObtainWord(String word) {
        showBehavior();
        requestData(word);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mDisposable != null)
            mDisposable.dispose();
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }
}
