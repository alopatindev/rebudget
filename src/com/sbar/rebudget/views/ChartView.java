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
    float m_rectW;
    float m_textY;
    float m_textPaddingX;

    @Override
    protected void onDraw(Canvas canvas) {
        m_lastAngle = 0.0f;
        //m_rectW = getWidth() > getHeight() ? getHeight() : getWidth();
        m_rectW = getWidth();
        m_textY = getWidth() > getHeight() ? 0.0f : m_rectW;
        m_textPaddingX = getWidth() > getHeight() ? m_rectW : 0.0f;

        for (Piece p : m_pieces)
            drawPiece(canvas, p);

        setMinimumHeight((int) (m_textY + m_rectW));
    }

    private void drawPiece(Canvas canvas, final Piece piece) {
        m_paint.setStyle(Paint.Style.FILL);

        m_paint.setTextSize(22);
        final float padding = m_paint.getFontSpacing();
        //final float angleSpace = 10.0f;
        final float angleSpace = 0.0f;

        m_paint.setColor(piece.color);
        canvas.drawText(piece.text0,
                        m_textPaddingX + padding,
                        m_textY + padding,
                        m_paint);
        m_textY += padding;

        // FIXME: we should check money instead of angles
        int moneyColor = piece.anglePlanned > piece.angleSpent
                         ? 0xffffffff : 0xffff0000;
        m_paint.setColor(moneyColor);
        m_paint.setTextSize(19);
        canvas.drawText(piece.text1,
                        m_textPaddingX + padding,
                        m_textY + padding,
                        m_paint);
        m_textY += padding * 2.0f;

        // remaining money
        m_paint.setColor(getDarkerColor(piece.color, 200));
        canvas.drawArc(new RectF(0, 0, m_rectW, m_rectW),
                       m_lastAngle + angleSpace,
                       piece.anglePlanned - angleSpace,
                       true,
                       m_paint);

        // spent money
        //final float stepPadding = 2.0f;
        final float stepPadding = 0.0f;
        m_paint.setColor(piece.color);
        canvas.drawArc(new RectF(stepPadding, stepPadding,
                                 m_rectW - stepPadding, m_rectW - stepPadding),
                       m_lastAngle + angleSpace/* + 0.7f*/,
                       piece.angleSpent - angleSpace/* - 1.0f*/,
                       true,
                       m_paint);

        m_lastAngle += piece.anglePlanned;
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

        // TODO: number bits formatting
        String spentText = String.format(
            "Spent %.2f of %.2f",
            moneySpent,
            moneyPlanned
        );

        m_pieces.add(new Piece(text, spentText, color, anglePlanned, angleSpent));
    }

    void sortPieces() {
        Collections.sort(m_pieces, new PieceComparator());
    }

    class Piece {
        public Piece(final String text0_, final String text1_,
                     int color_, float anglePlanned_, float angleSpent_) {
            text0 = text0_;
            text1 = text1_;
            color = color_;
            anglePlanned = anglePlanned_;
            angleSpent = angleSpent_;
        }
        public String text0;
        public String text1;
        public int color;
        public float anglePlanned;
        public float angleSpent;
    }

    class PieceComparator implements Comparator<Piece> {
        @Override
        public int compare(Piece p1, Piece p2) {
            return (int) ((p2.anglePlanned + p2.angleSpent) -
                          (p1.anglePlanned + p1.angleSpent));
        }
    }
}
