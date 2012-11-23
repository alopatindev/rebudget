package com.sbar.rebudget.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.*;
import android.view.View;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;

import com.sbar.rebudget.activities.MainTabActivity;

public class ChartView extends View {
    //private ShapeDrawable mDrawable;
    Paint m_paint = new Paint();

    ArrayList<Piece> m_pieces = new ArrayList<Piece>();  // sorted by angles

    //float m_totalMoney = 0.0;
    float m_totalMoney = 15000.0f + 5000.0f + 3000.0f;

    public ChartView(Context context) {
        super(context);
        m_paint.setAntiAlias(true);

        addPiece("Reserved", 0xff0000ff, 15000.0f, 4050.10f);
        addPiece("Food", 0x00ff00ff, 5000.0f, 1040.85f);
        addPiece("Misc", 0x0000ffff, 3000.0f, 2040.85f);
        sortPieces();
    }

    float m_lastAngle;

    @Override
    protected void onDraw(Canvas canvas) {
        MainTabActivity.LOGI("onDraw");
        m_lastAngle = 0.0f;
        for (Piece p : m_pieces)
            drawPiece(canvas, p);
    }

    private void drawPiece(Canvas canvas, final Piece piece) {
        m_paint.setStyle(Paint.Style.FILL);

        float rectW = getWidth() > getHeight() ? getHeight() : getWidth();

        m_paint.setColor(piece.color);
        canvas.drawArc(new RectF(0, 0, rectW, rectW),
                       m_lastAngle,
                       m_lastAngle + piece.angleSpent,
                       true,
                       m_paint);
        m_lastAngle += piece.angleSpent;

        m_paint.setColor(getDarkerColor(piece.color, 50));
        canvas.drawArc(new RectF(0, 0, rectW, rectW),
                       m_lastAngle,
                       m_lastAngle + piece.anglePlanned,
                       true,
                       m_paint);
        m_lastAngle += piece.anglePlanned;

        //m_paint.setColor(0x880000ff);
        //canvas.drawArc(new RectF(0, 0, 300, 50), 0.0f, 170.0f, true, m_paint);
    }

    int getDarkerColor(int color, int darker) {
        int r = (color >> (3*8)) & 0xFF;
        int g = (color >> (2*8)) & 0xFF;
        int b = (color >> (1*8)) & 0xFF;
        r -= darker;
        g -= darker;
        b -= darker;
        int a = 0xFF;
        if (r < 0) r = 0;
        if (g < 0) g = 0;
        if (b < 0) b = 0;
        int dcolor = (r << (3*8)) | (g << (2*8)) | (b << (1*8)) | (a << (0*8));
        return dcolor;
    }

    void addPiece(final String text, int color,
                  float moneyPlanned, float moneySpent) {
        // m_totalMoney - 360
        // planned - ?
        float anglePlanned = (moneyPlanned * 360.0f) / m_totalMoney;
        float angleSpent = (moneySpent * 360.0f) / m_totalMoney;
        m_pieces.add(new Piece(text, color, anglePlanned, angleSpent));
    }

    void sortPieces() {
        Collections.sort(m_pieces, new PieceComparator());
    }

    class Piece {
        public Piece(final String text_, int color_,
                     float anglePlanned_, float angleSpent_) {
            text = text_;
            color = color_;
            anglePlanned = anglePlanned_;
            angleSpent = angleSpent_;
        }
        public String text;
        public int color;
        public float anglePlanned;
        public float angleSpent;
    }

    class PieceComparator implements Comparator<Piece> {
        @Override
        public int compare(Piece p1, Piece p2) {
            return (int) ((p1.anglePlanned + p1.angleSpent) -
                          (p2.anglePlanned + p2.angleSpent));
        }
    }
}
