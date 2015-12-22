package com.xwj.xwjnote3.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.xwj.xwjnote3.R;
import com.xwj.xwjnote3.model.Note;
import com.xwj.xwjnote3.presenter.Presenter;
import com.xwj.xwjnote3.presenter.impl.PresenterImpl;
import com.xwj.xwjnote3.ui.NoteActivity;
import com.xwj.xwjnote3.utils.CommonUtils;
import com.xwj.xwjnote3.utils.ConstantUtils;
import com.xwj.xwjnote3.view.NoteListView;

import java.util.List;


/**
 * Note的Item的适配器。
 * Created by SherVegas on 2015/12/3.
 */
public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> implements View.OnClickListener, View.OnLongClickListener, CompoundButton.OnCheckedChangeListener {

    private Context mContext;
    private List<Note> mNotes;
    private LayoutInflater mInflater;
    private PopupMenu mPopupMenu;
    private Presenter mPresenter;
    private boolean mIsInTrash;


    public NoteAdapter(Context context, List<Note> notes, boolean isInTrash) {
        mContext = context;
        mNotes = notes;
        mInflater = LayoutInflater.from(context);
        mIsInTrash = isInTrash;
        mPresenter = new PresenterImpl((NoteListView) context);

    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.item_note, parent, false);//important!!!!
        return new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        if (!mIsInTrash) {
            holder.itemView.setOnClickListener(this);
            holder.checkBox.setVisibility(View.VISIBLE);
        } else {
            holder.checkBox.setVisibility(View.INVISIBLE);
        }
        holder.itemView.setOnLongClickListener(this);
        holder.checkBox.setTag(mNotes.get(position));
        holder.checkBox.setOnCheckedChangeListener(this);
        holder.itemView.setTag(mNotes.get(position));
        holder.textView.setText(mNotes.get(position).getTitle());
        holder.checkBox.setChecked(mNotes.get(position).getFavorite());
        holder.textViewCreateTime.setText(CommonUtils.getDate(mContext, mNotes.get(position).getCreateTime()));
    }

    @Override
    public int getItemCount() {
        return mNotes.size();
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(mContext, NoteActivity.class);
        intent.setAction(ConstantUtils.ACTION_EDIT);
        intent.putExtra("note", (Note) v.getTag());
        mContext.startActivity(intent);
    }

    @Override
    public boolean onLongClick(View v) {
        final CheckBox chk = (CheckBox) v.findViewById(R.id.chk_note);
        mPopupMenu = new PopupMenu(mContext, v);
        if (!mIsInTrash) {
            mPopupMenu.inflate(R.menu.popup_menu);
        } else {
            mPopupMenu.inflate(R.menu.popup_menu_trash);
        }
        mPopupMenu.show();
        final Note note = (Note) v.getTag();
        mPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.popup_delete:
                        if (note.getFavorite()) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                            builder.setTitle("此便签已收藏，确定移入回收站？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mPresenter.moveToTrash(note);
                                    removeFromDataset(note);
                                }
                            }).setNegativeButton("取消", null).show();
                        } else {
                            mPresenter.moveToTrash(note);
                            removeFromDataset(note);
                        }
                        break;
                    case R.id.popup_share:
                        mPresenter.shareNote(note);
                        break;
                    case R.id.popup_delete_from_db:
                        Toast.makeText(mContext, "彻底删除", Toast.LENGTH_SHORT).show();
                        mPresenter.deleteNote(note);
                        removeFromDataset(note);
                        break;
                    case R.id.popup_restore:
                        Toast.makeText(mContext, "恢复", Toast.LENGTH_SHORT).show();
                        mPresenter.restoreNote(note);
                        removeFromDataset(note);
                        break;
                    case R.id.popup_favorite:
                        mPresenter.favoriteNote(note, !note.getFavorite());
                        chk.setChecked(note.getFavorite());
                }
                return false;
            }
        });
        return true;
    }


    /**
     * 从数据列表中移除指定位置的note。
     *
     * @param note
     */
    private void removeFromDataset(Note note) {
        int index;
        index = mNotes.indexOf(note);
        mNotes.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        Note note = (Note) buttonView.getTag();
        mPresenter.favoriteNote(note, isChecked);
    }


    static class NoteViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        CheckBox checkBox;
        TextView textViewCreateTime;

        public NoteViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.tv_note);
            checkBox = (CheckBox) itemView.findViewById(R.id.chk_note);
            textViewCreateTime = (TextView) itemView.findViewById(R.id.tv_create_time_item);
        }
    }

    public void clear() {
        mNotes.clear();
        notifyDataSetChanged();
    }
}
