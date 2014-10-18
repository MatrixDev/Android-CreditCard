package dev.matrix.creditCard;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Spanned;
import android.text.method.TransformationMethod;
import android.text.style.ReplacementSpan;
import android.view.View;

import java.lang.reflect.Array;

/**
 * @author rostyslav.lesovyi
 */
public class CreditCardTransformation implements TransformationMethod {

	private static final Divider sDivider = new Divider();

	@Override
	public CharSequence getTransformation(final CharSequence source, View view) {
		return new SpannedWrapper(source) {
			@Override
			@SuppressWarnings("unchecked")
			public <T> T[] getSpans(int start, int end, Class<T> type) {
				T[] array = super.getSpans(start, end, type);
				if (!type.isAssignableFrom(Divider.class)) {
					return array;
				}
				Object[] result = (Object[]) Array.newInstance(type, array.length + 1);
				result[0] = sDivider;
				System.arraycopy(array, 0, result, 1, array.length);
				return (T[]) result;
			}

			@Override
			public int getSpanStart(Object tag) {
				if (tag instanceof Divider) {
					return 0;
				}
				return super.getSpanStart(tag);
			}

			@Override
			public int getSpanEnd(Object tag) {
				if (tag instanceof Divider) {
					return length();
				}
				return super.getSpanEnd(tag);
			}
		};
	}

	@Override
	public void onFocusChanged(View view, CharSequence sourceText, boolean focused, int direction, Rect previouslyFocusedRect) {
	}

	static class SpannedWrapper implements Spanned {
		private Spanned mSpanned;
		private CharSequence mSource;

		SpannedWrapper(CharSequence source) {
			mSource = source;
			if (mSource instanceof Spanned) {
				mSpanned = (Spanned) source;
			}
		}

		@Override
		@SuppressWarnings("unchecked")
		public <T> T[] getSpans(int start, int end, Class<T> type) {
			if (mSpanned != null) {
				return mSpanned.getSpans(start, end, type);
			}
			return (T[]) Array.newInstance(type, 0);
		}

		@Override
		public int getSpanStart(Object tag) {
			if (mSpanned != null) {
				return mSpanned.getSpanStart(tag);
			}
			return 0;
		}

		@Override
		public int getSpanEnd(Object tag) {
			if (mSpanned != null) {
				return mSpanned.getSpanEnd(tag);
			}
			return 0;
		}

		@Override
		public int getSpanFlags(Object tag) {
			if (mSpanned != null) {
				return mSpanned.getSpanFlags(tag);
			}
			return 0;
		}

		@Override
		public int nextSpanTransition(int start, int limit, Class type) {
			if (mSpanned != null) {
				return mSpanned.nextSpanTransition(start, limit, type);
			}
			return 0;
		}

		@Override
		public int length() {
			return mSource.length();
		}

		@Override
		public char charAt(int index) {
			return mSource.charAt(index);
		}

		@Override
		public CharSequence subSequence(int start, int end) {
			return mSource.subSequence(start, end);
		}

		@Override
		public String toString() {
			return mSource.toString();
		}
	}

	static class Divider extends ReplacementSpan {
		@Override
		public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
			int groups = Math.min(3, (end - start) / 4);
			return (int) (paint.measureText(text, start, end) + paint.getTextSize() * groups);
		}

		@Override
		public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
			int offset = 0;
			int groupStart = start;
			while (groupStart < end) {
				int groupEnd = Math.min(groupStart + 4, end);
				canvas.drawText(text, groupStart, groupEnd, x + offset, y, paint);
				offset += paint.measureText(text, groupStart, groupEnd) + paint.getTextSize();
				groupStart = groupEnd;
			}
		}
	}
}
