package com.daniillshei.sceditor;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class CustomTable extends TableLayout {
    LinearLayout header;
    LayoutInflater inflater;

    //    float[] presetColumnWidthPercentage;
    int[] columnWidth;

    public CustomTable(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        inflater = LayoutInflater.from(context);
        if (isInEditMode()) {
            setColumnsWidth(new float[]{0.5F, 0.5F}, getWidth());
            setHeader(new String[]{"Preview", "Only"});
            addRow(new String[]{"12", "34"});
            addRow(new String[]{"5678", "910"});
//            setHeight(300);
//            setMinimumHeight(300);
        }
    }

    public void setColumnsWidth(float[] widths, int width) {
        columnWidth = new int[widths.length];
        for (int i = 0; i < widths.length; i++) {
            columnWidth[i] = (int) (width * widths[i]);
        }
    }

    public void setHeader(String[] headers) {
        header = createRow(headers);
        GradientDrawable divider = new GradientDrawable();
        divider.setColor(Color.GRAY); // Divider color
        divider.setSize(1, 1);        // 1px thickness
        header.setDividerDrawable(divider);
        header.setShowDividers(SHOW_DIVIDER_MIDDLE);
        for (int i = 0; i < headers.length; i++) {
            int finalI = i;
            header.getChildAt(i).setOnClickListener(v -> {
                sortTable(finalI);
            });
        }
    }

    public LinearLayout addRow(String[] text) {
        LinearLayout row = createRow(text);
        addView(row);
        return row;
    }

    public LinearLayout addRow(Object... rowData) {
        var texts = new String[rowData.length];
        for (int i = 0; i < rowData.length; i++) {
            texts[i] = rowData[i] == null ? null : rowData[i].toString();
        }
        return addRow(texts);
    }

    public LinearLayout createRow(String[] text) {
        var row = (LinearLayout) inflater.inflate(R.layout.table_row, this, false);
        for (int i = 0; i < text.length; i++) {
            var column = (TextView) inflater.inflate(R.layout.table_row_column, row, false);
            column.setText(text[i]);
            column.setLayoutParams(new LinearLayout.LayoutParams(
                    columnWidth[i],
                    ViewGroup.LayoutParams.MATCH_PARENT
            ));
            row.addView(column);
        }
        return row;
    }

    private void sortTable(int columnIndex) {
        int rowCount = getChildCount();
        if (rowCount <= 1) return;

        // Extract rows into a list
        List<View> rows = new ArrayList<>();
        for (int i = 0; i < rowCount; i++) {
            rows.add(getChildAt(i));
        }

        // Sort rows based on the text of the selected column
        rows.sort((a, b) -> {
            LinearLayout rowA = (LinearLayout) a;
            LinearLayout rowB = (LinearLayout) b;

            TextView textA = (TextView) rowA.getChildAt(columnIndex);
            TextView textB = (TextView) rowB.getChildAt(columnIndex);

            String stringA = textA.getText().toString();
            if (stringA.isEmpty()) stringA = "zzz";
            String stringB = textB.getText().toString();
            if (stringB.isEmpty()) stringB = "zzz";
            return columnIndex == 0 ? Integer.parseInt(stringA) - Integer.parseInt(stringB) : stringA.compareTo(stringB);
        });

        // Re-attach rows in sorted order
        removeAllViews();
        for (View row : rows) {
            addView(row);
        }
    }

//    public void setHeight(int px) {
//        setLayoutParams(new LinearLayout.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT,
//                px
//        ));
//    }
}
