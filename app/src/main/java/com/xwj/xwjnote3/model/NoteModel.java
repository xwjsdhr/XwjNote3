package com.xwj.xwjnote3.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xwjsd on 2015/12/4.
 */
public interface NoteModel {

    void addNote(Note note);

    void deleteNote(Note note);

    void updateNote(Note note);

    Note getNote(String id);

    ArrayList<Note> getAllNotes();

    void moveToTrashBin(Note note);

    ArrayList<Note> getNotes(String type);

    ArrayList<Note> getNotesByTitle(String content, boolean b);

    void restoreNote(Note note);

    ArrayList<Note> getAllFavoriteNotes();

    void clearAllTrash();

    List<Note> addAllNotes(List<Note> list);
}
