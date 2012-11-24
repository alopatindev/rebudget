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
    float m_totalMoney = 100.0f;

    public ChartView(Context context) {
        super(context);
        m_paint.setAntiAlias(true);

        addPiece("Reserved", 0xffff0000, 50.0f, 20.10f);
        addPiece("Food", 0xff00ff00, 40.0f, 45.0f);
        addPiece("Misc", 0xff0000ff, 10.0f, 5.0f);
        sortPieces();
    }

    float m_lastAngle;

    @Override
    protected void onDraw(Canvas canvas) {
        m_lastAngle = 0.0f;
        for (Piece p : m_pieces)
            drawPiece(canvas, p);
    }

    private void drawPiece(Canvas canvas, final Piece piece) {
        m_paint.setStyle(Paint.Style.FILL);

        float rectW = getWidth() > getHeight() ? getHeight() : getWidth();

        m_paint.setColor(getDarkerColor(piece.color, 0x80));
        canvas.drawArc(new RectF(0, 0, rectW, rectW),
                       m_lastAngle,
                       piece.angleSpent,
                       true,
                       m_paint);
        m_lastAngle += piece.angleSpent;

        // remaining
        if (piece.anglePlanned - piece.angleSpent <= 0)
            return;

        m_paint.setColor(piece.color);
        canvas.drawArc(new RectF(0, 0, rectW, rectW),
                       m_lastAngle,
                       piece.anglePlanned - piece.angleSpent,
                       true,
                       m_paint);
        m_lastAngle += (piece.anglePlanned - piece.angleSpent);
    }

    int getDarkerColor(int color, int darker) {
        int a = 0xFF;
        int r = (color >> (2*8)) & 0xFF;
        int g = (color >> (1*8)) & 0xFF;
        int b = (color >> (0*8)) & 0xFF;
        r -= darker;
        g -= darker;
        b -= darker;
        if (r < 0) r = 0;
        if (g < 0) g = 0;
        if (b < 0) b = 0;
        if (r > 0xFF) r = 0xFF;
        if (g > 0xFF) g = 0xFF;
        if (b > 0xFF) b = 0xFF;
        int dcolor = (a << (3*8)) | (r << (2*8)) | (g << (1*8)) | (b << (0*8));
        return dcolor;
    }

    void addPiece(final String text, int color,
                  float moneyPlanned, float moneySpent) {
        // m_totalMoney - 360
        // planned - ?
        float anglePlanned = (moneyPlanned * 360.0f) / m_totalMoney;
        float angleSpent = (moneySpent * 360.0f) / m_totalMoney;
        if (angleSpent > anglePlanned)
            angleSpent = anglePlanned;
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
