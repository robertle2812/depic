package com.frankemerald.depicpro.Models;

import android.graphics.Typeface;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Mark on 04.11.2015.
 */
public class Text {

    private int id;
    private int color;
    private ViewGroup.LayoutParams params;
    private int font;
    private int radius;
    private int[] coord;
    private String text;
    private EditText editText;
    private TextView textView;
    private ImageView delete, resize;
    private RelativeLayout border, move;
    private FrameLayout frame;


    public Text(RelativeLayout move, FrameLayout frame, RelativeLayout border, int color, ImageView delete, int[] coord, EditText editText, int font, int id, ViewGroup.LayoutParams params, int radius, ImageView resize, TextView textView, String text) {
        this.border = border;
        this.color = color;
        this.delete = delete;
        this.coord = coord;
        this.editText = editText;
        this.font = font;
        this.id = id;
        this.params = params;
        this.radius = radius;
        this.resize = resize;
        this.textView = textView;
        this.text = text;
        this.frame = frame;
        this.move = move;
    }

    public FrameLayout getFrame(){
        return frame;
    }

    public RelativeLayout getMove(){
        return move;
    }

    public void setBorder(RelativeLayout border) {
        this.border = border;
    }

    public void setCoord(int[] coord) {
        this.coord = coord;
    }

    public void setDelete(ImageView delete) {
        this.delete = delete;
    }

    public void setEditText(EditText editText) {
        this.editText = editText;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void setResize(ImageView resize) {
        this.resize = resize;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public RelativeLayout getBorder() {

        return border;
    }

    public int getColor() {
        return color;
    }

    public int[] getCoord() {
        return coord;
    }

    public ImageView getDelete() {
        return delete;
    }

    public EditText getEditText() {
        return editText;
    }

    public int getFont() {
        return font;
    }

    public ViewGroup.LayoutParams getParams() {
        return params;
    }

    public int getRadius() {
        return radius;
    }

    public ImageView getResize() {
        return resize;
    }

    public String getText() {
        return text;
    }

    public TextView getTextView() {
        return textView;
    }

    public Text(ViewGroup.LayoutParams params, String text, int font, int color) {
        this.params = params;
        this.font = font;
        this.color = color;

    }



    public Text(ViewGroup.LayoutParams layoutParams, Object font, int black) {
    }

    public int getId(){
        return id;
    }
    public void setColor(int color) {
        this.color = color;
    }

    public void setParams(ViewGroup.LayoutParams params) {
        this.params = params;
    }

    public void setFont(int font) {
        this.font = font;
    }

    public void setText(String text){
        this.text = text;
    }
}
