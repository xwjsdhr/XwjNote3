package com.xwj.xwjnote3.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.xwj.xwjnote3.R;
import com.xwj.xwjnote3.adapter.NoteAdapter;
import com.xwj.xwjnote3.model.MyUser;
import com.xwj.xwjnote3.model.Note;
import com.xwj.xwjnote3.presenter.Presenter;
import com.xwj.xwjnote3.presenter.impl.PresenterImpl;
import com.xwj.xwjnote3.utils.ConstantUtils;
import com.xwj.xwjnote3.utils.PreferenceUtils;
import com.xwj.xwjnote3.view.NoteListView;

import java.util.List;

import cn.bmob.v3.Bmob;


/**
 * 主界面
 *
 * @author SherVegas
 */
public class MainActivity extends AppCompatActivity implements NoteListView, View.OnClickListener, SearchView.OnQueryTextListener, SearchView.OnCloseListener, View.OnFocusChangeListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private FloatingActionButton mFab;
    private DrawerLayout mDrawerLayout;
    private RecyclerView mRvMain;
    private NoteAdapter mNoteAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ProgressBar mPbLoading;
    private Presenter mPresenter;
    private RadioGroup mRgDrawer;
    private Toolbar mToolbar;
    private GridLayoutManager mGridLayoutManager;
    private LinearLayoutManager mLinearLayoutManager;
    private StaggeredGridLayoutManager mStaggeredGridLayoutManager;
    private int mLayoutIndex = 0;
    private boolean isInTrash = false;
    private TextView mTvUsername;
    private List<Note> mList;

    public DrawerLayout getDrawerLayout() {
        return mDrawerLayout;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bmob.initialize(this, "1cb9fad5114b9a6bebe365c5800db5e9");
        initMainView();
        initDrawerView();
        mPresenter = new PresenterImpl(this);
    }

    /**
     * 初始化Drawer布局
     */
    private void initDrawerView() {
        mTvUsername = (TextView) this.findViewById(R.id.tv_drawer_username);
        mTvUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.startUserInfoActivity();
            }
        });
        mRgDrawer = (RadioGroup) this.findViewById(R.id.rg_drawer);
        mRgDrawer.setOnCheckedChangeListener(new MyOnCheckedChangeListener());
    }

    /**
     * 添加当查询文本提交的监听。
     *
     * @param query
     * @return
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    /**
     * 添加当查询文本改变的监听
     *
     * @param newText
     * @return
     */
    @Override
    public boolean onQueryTextChange(String newText) {
        mPresenter.searchByContent(newText, isInTrash);
        return true;
    }

    /**
     * 当查询文本框关闭的监听
     *
     * @return
     */
    @Override
    public boolean onClose() {
        mPresenter.onResume();
        Toast.makeText(this, "close", Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            if (isInTrash) {
                mSearchView.setQueryHint("查询回收站内的便签");
            } else {
                mSearchView.setQueryHint("查询便签");
            }
        }
    }

    @Override
    public void onRefresh() {
        mPresenter.syncNote(mList);
        mNoteAdapter.notifyDataSetChanged();
    }

    /**
     * 选中改变的监听类
     */
    class MyOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            RadioButton button = (RadioButton) group.getChildAt(checkedId - 1);
            CharSequence title = button.getText();
            if (title.equals("便签")) {
                isInTrash = false;
            } else if (title.equals("回收站")) {
                isInTrash = true;
            }
            mToolbar.setTitle(title);
            mPresenter.switchList(checkedId - 1);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mActionBarDrawerToggle.syncState();
    }

    class MyOnScrollListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                hideFloatActionButton();
            } else {
                showFloatActionButton();
            }
            super.onScrollStateChanged(recyclerView, newState);
        }
    }

    /**
     * 初始化视图
     */
    private void initMainView() {

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_main);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.sl_main);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mRvMain = (RecyclerView) findViewById(R.id.rv_main);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mPbLoading = (ProgressBar) findViewById(R.id.pb_loading);
        mRvMain.addOnScrollListener(new MyOnScrollListener());
        mRvMain.setItemAnimator(new DefaultItemAnimator());
        mRvMain.setHasFixedSize(true);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, 0, 0) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                mToolbar.setTitle(R.string.title_choose);
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                if (newState == DrawerLayout.STATE_IDLE && mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    int checkedRadioButtonId = mRgDrawer.getCheckedRadioButtonId();
                    if (checkedRadioButtonId == -1) {
                        mRgDrawer.check(1);
                    }
                } else if (newState == DrawerLayout.STATE_IDLE && !mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    int checkedRadioButtonId = mRgDrawer.getCheckedRadioButtonId();
                    if (checkedRadioButtonId != -1) {
                        RadioButton button = (RadioButton) mRgDrawer.getChildAt(checkedRadioButtonId - 1);
                        String title = (String) button.getText();
                        mToolbar.setTitle(title);
                    } else {
                        mRgDrawer.check(1);
                    }
                }
                super.onDrawerStateChanged(newState);
            }
        };
        mGridLayoutManager = new GridLayoutManager(this, 2);
        mLinearLayoutManager = new LinearLayoutManager(this);
        mStaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, RecyclerView.VERTICAL);
        mActionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        mRvMain.addItemDecoration(new MyItemDecoration());
        setListener();
    }

    /**
     * 设置监听器。
     */
    private void setListener() {
        mFab.setOnClickListener(this);
        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);
    }

    @Override
    protected void onResume() {
        mPresenter.onResume();
        super.onResume();
    }

    /**
     * 显示进度条
     */
    @Override
    public void showProgress() {
        mPbLoading.setVisibility(View.VISIBLE);
        mRvMain.setVisibility(View.GONE);
    }

    /**
     * 隐藏进度条
     */
    @Override
    public void hideProgress() {
        mPbLoading.setVisibility(View.GONE);
        mRvMain.setVisibility(View.VISIBLE);
    }

    /**
     * 设置数据
     *
     * @param list
     */
    @Override
    public void setList(List<Note> list) {
        mLayoutIndex = PreferenceUtils.getInt(this, PreferenceUtils.INDEX_LAYOUTMANAGER);
        setLayoutManager(mLayoutIndex);
        switchFabIcon();
        mList = list;
        mNoteAdapter = new NoteAdapter(this, mList, isInTrash);
        mRvMain.setAdapter(mNoteAdapter);
    }


    @Override
    public void switchRecyclerView(List<Note> list) {
        mNoteAdapter = new NoteAdapter(this, list, isInTrash);
        setLayoutManager(mLayoutIndex);
        mRvMain.setAdapter(mNoteAdapter);
    }

    /**
     * 关闭抽屉
     */
    @Override
    public void closeDrawer() {
        mDrawerLayout.closeDrawers();
    }

    /**
     * 打开抽屉
     */
    @Override
    public void openDrawer() {
        mDrawerLayout.openDrawer(Gravity.LEFT);
    }

    @Override
    public void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setLayoutManager(int index) {
        mLayoutIndex = index;
        if (index == 0) {
            mRvMain.setLayoutManager(mLinearLayoutManager);
        } else if (index == 1) {
            mRvMain.setLayoutManager(mGridLayoutManager);
        } else if (index == 2) {
            mRvMain.setLayoutManager(mStaggeredGridLayoutManager);
        }
        PreferenceUtils.setInt(this, PreferenceUtils.INDEX_LAYOUTMANAGER, index);
    }

    /**
     * 显示选择布局的对话框
     */
    @Override
    public void showChooseLayoutManagerDialog() {

        String[] layouts = {
                "线性布局",
                "网格布局",
                "瀑布流布局"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("选择布局")
                .setSingleChoiceItems(layouts, mLayoutIndex, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setLayoutManager(which);
                        dialog.dismiss();
                    }
                }).show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                if (!isInTrash) {
                    Intent intent = new Intent(this, NoteActivity.class);
                    intent.setAction(ConstantUtils.ACTION_NEW);
                    startActivity(intent);
                } else {
                    showClearTrashBinDialog();
                }
                break;
        }
    }

    /**
     * 显示清空回收站的对话框
     */
    private void showClearTrashBinDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("清空回收站？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.clearTrashBin();
                        NoteAdapter adapter = (NoteAdapter) mRvMain.getAdapter();
                        adapter.clear();
                    }
                }).setNegativeButton("取消", null).show();
    }

    private SearchView mSearchView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        //mSearchView.setQueryHint("输入要查找的标题");
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setOnQueryTextFocusChangeListener(this);
        mSearchView.setOnCloseListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //在网格布局和线性布局之间切换
        if (item.getItemId() == R.id.action_switch) {
            showChooseLayoutManagerDialog();
        } else if (item.getItemId() == R.id.action_settings) {
            startActivity(new Intent(this, SettingActivity.class));
        } else if (item.getItemId() == R.id.action_sync) {
            //mPresenter.syncNote();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showFloatActionButton() {
        mFab.show();
    }

    @Override
    public void hideFloatActionButton() {
        mFab.hide();
    }

    @Override
    public void switchFabIcon() {
        if (isInTrash) {
            mFab.setImageResource(R.drawable.ic_delete_white_36dp);
        } else {
            mFab.setImageResource(R.drawable.ic_create_white_36dp);
        }
    }

    @Override
    public void setUserInfo(MyUser userInfo) {
        mTvUsername.setText(userInfo.getUsername());
    }

    @Override
    public void showSwipeProgress() {
        mSwipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideSwipeProgress() {
        mSwipeRefreshLayout.setRefreshing(false);
    }
}
