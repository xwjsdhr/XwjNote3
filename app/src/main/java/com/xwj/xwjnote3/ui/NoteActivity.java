package com.xwj.xwjnote3.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.rengwuxian.materialedittext.MaterialEditText;
import com.xwj.xwjnote3.R;
import com.xwj.xwjnote3.model.Note;
import com.xwj.xwjnote3.presenter.NotePresenter;
import com.xwj.xwjnote3.presenter.impl.NotePresenterImpl;
import com.xwj.xwjnote3.utils.CommonUtils;
import com.xwj.xwjnote3.utils.ConstantUtils;
import com.xwj.xwjnote3.view.NoteView;


public class NoteActivity extends AppCompatActivity implements NoteView, View.OnClickListener, View.OnFocusChangeListener {

    private static final String TAG = NoteActivity.class.getSimpleName();
    private static final int REQ_LOAD_IMAGE = 1;
    private NotePresenter mNotePresenter;
    private MaterialEditText materialEditTextContent;
    private MaterialEditText materialEditTextTitle;
    private Note mNote = null;
    private String mAction = "";
    private Toolbar mToolbar;
    private String mContent = "";
    private TextView mTvCreateTime, mTvLastModifiedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        initView();
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        materialEditTextContent = (MaterialEditText) this.findViewById(R.id.tv_content);
        materialEditTextContent.setTransformationMethod(null);
        materialEditTextTitle = (MaterialEditText) this.findViewById(R.id.tv_title);
        mTvCreateTime = (TextView) this.findViewById(R.id.tv_create_time);
        mTvLastModifiedTime = (TextView) this.findViewById(R.id.tv_last_modified_time);

        mNote = (Note) getIntent().getSerializableExtra("note");
        mAction = getIntent().getAction();
        mNotePresenter = new NotePresenterImpl(this, this);
        mNotePresenter.bindDataByNote(mNote);
        setToolbarIconAndTitle(mToolbar);
        mToolbar.setNavigationOnClickListener(this);
        materialEditTextTitle.setOnFocusChangeListener(this);
        materialEditTextContent.setOnFocusChangeListener(this);
    }


    private void setToolbarIconAndTitle(Toolbar toolbar) {
        if (mAction.equals(ConstantUtils.ACTION_EDIT)) {
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
            toolbar.setTitle("编辑便签");
            mContent = mNote.getContent();
        } else {
            toolbar.setNavigationIcon(R.drawable.ic_done_white_24dp);
            toolbar.setTitle("新建便签");
        }
    }


    @Override
    public void bind(Note note) {
        materialEditTextTitle.setText(note.getTitle());
        materialEditTextContent.setText(note.getContent());
        mTvCreateTime.setText("创建时间：" + CommonUtils.getDate(this, note.getCreateTime()));
        mTvLastModifiedTime.setText("最近修改时间：" + CommonUtils.getDate(this, note.getLastModifiedTime()));
        SpannableStringBuilder builder = new SpannableStringBuilder("");


    }

    @Override
    public void setContentHint(String hint) {
        //mEtContent.setHint(hint);
    }

    /**
     * @return Note的内容
     */
    @Override
    public String getNoteContent() {
        return materialEditTextContent.getText().toString();
    }

    /**
     * @return 获取Note的标题。
     */
    @Override
    public String getNoteTitle() {
        return materialEditTextTitle.getText().toString();
    }

    /**
     * 关闭当前Activity
     */
    @Override
    public void finishActivity() {
        this.finish();
    }


    @Override
    public void pickImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQ_LOAD_IMAGE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQ_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
//            Uri selectedImage = data.getData();
//            Bitmap bitmap = null;
//            try {
//                bitmap = Picasso.with(this).load(selectedImage).config(Bitmap.Config.ALPHA_8).get();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            Drawable drawable = new BitmapDrawable(getResources(), bitmap);
//            materialEditTextContent.setCompoundDrawables(null, null, null, drawable);
//        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        if (materialEditTextTitle.isFocused() || materialEditTextContent.isFocused()) {
            mNotePresenter.save(mAction, mNote, true);
        } else {
            finishActivity();
        }
    }

    /**
     * 焦点改变
     *
     * @param v
     * @param hasFocus
     */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            if (mAction.equals(ConstantUtils.ACTION_EDIT)) {
                mToolbar.setNavigationIcon(R.drawable.ic_done_white_24dp);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (materialEditTextTitle.isFocused() || materialEditTextContent.isFocused()) {
            showTextChangeDialog();
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 创建提示文本已经改变是否修改的对话框。
     */
    private void showTextChangeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("便签内容已改变，是否修改?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mNotePresenter.save(mAction, mNote, true);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mNotePresenter.save(mAction, mNote, false);
            }
        }).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_attach:
                this.pickImage();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
