package com.irate.nguyet.common;

import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import kotlin.collections.CollectionsKt;
import kotlin.text.StringsKt;

public final class ViewExt {
    @NonNull
    public static <T> List<T> getViewsByType(@NotNull ViewGroup parent, @NotNull Class<T> cls) {
        List<T> result = new ArrayList<>();
        for (int childIndex = 0; childIndex < parent.getChildCount(); ++childIndex) {
            View child = parent.getChildAt(childIndex);
            if (child instanceof ViewGroup) {
                result.addAll(getViewsByType((ViewGroup) child, cls));
            } else if (cls.isInstance(child)) {
                result.add(cls.cast(child));
            }
        }
        return result;
    }

    @Nullable
    public static String requiredText(@NotNull TextInputLayout inputLayout, @NotNull String errorMessage) {
        EditText editText = CollectionsKt.firstOrNull(getViewsByType(inputLayout, EditText.class));
        String text = String.valueOf(editText != null ? editText.getText() : null);
        String var3;
        if (StringsKt.isBlank(text)) {
            inputLayout.setError(errorMessage);
            inputLayout.requestFocus();
            var3 = null;
        } else {
            inputLayout.setError(null);
            var3 = text;
        }

        return var3;
    }

    public static void clearText(@NotNull TextInputLayout inputLayout) {
        inputLayout.setError(null);
        List<TextInputEditText> inputLayouts = getViewsByType(inputLayout, TextInputEditText.class);
        for (TextInputEditText editText : inputLayouts) {
            editText.setText(null);
        }
    }
}
