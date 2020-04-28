package com.uestc.bookreader.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Vector;

public class BookPageBezierHelper {

    public static final String GBK = "GBK";
    public static final String UTF_8 = "UTF-8";
    public static final String UNICODE = "Unicode";
    public static final String UTF_16_BE = "UTF-16BE";
    public static final String UTF_16_LE = "UTF-16LE";
    private int mWidth;
    private int mHeight;
    private File mBookFile;
    private int mBookBufferLength;
    private MappedByteBuffer mBookBuffer;
    private String mBookCharsetName = GBK;
    private int mPageLineCount; // 每页可以显示的行数
    private int mBufferBegin;
    private int mBufferEnd;
    private float mVisibleHeight; // 绘制内容的高
    private float mVisibleWidth; // 绘制内容的宽
    private Paint mPaint;
    private boolean mIsFirstPage, mIsLastPage;
    private Vector<String> mLinesVector = new Vector<>();
    private boolean mIsUserBg = false;

    private OnProgressChangedListener onProgressChangedListener;

    private Bitmap mBookBgBitmap;
    private int mBackGroundColor = 0xffff9e85; // 背景颜色
    private int mMarginWidth = 35; // 左右与边缘的距离
    private int mMarginHeight = 80; // 上下与边缘的距离
    private int mFontSize = 60;
    private int mLineMargin = 5;
    private int mTextColor = Color.BLACK;


    public BookPageBezierHelper(int width, int height) {
        this(width, height,Color.WHITE, Color.BLACK, 50, 60, 0);
    }
    public BookPageBezierHelper(int width, int height, int progress) {
        this(width, height,Color.WHITE, Color.BLACK, 50, 60, progress);
    }

    public BookPageBezierHelper(int width, int height, int backGroundColor, int textColor, int lineMargin, int fontSize, int progress) {
        mWidth = width;
        mHeight = height;
        mTextColor = textColor;
        mBackGroundColor = backGroundColor;
        mLineMargin = lineMargin;
        mFontSize = fontSize;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setTextAlign(Paint.Align.LEFT);
        mPaint.setTextSize(mFontSize);
        mPaint.setColor(mTextColor);
        mPaint.setTypeface(null);
        mVisibleWidth = mWidth - mMarginWidth * 2;
        mVisibleHeight = mHeight - mMarginHeight * 2 + 108;
        mPageLineCount = (int) (mVisibleHeight / (mFontSize + mLineMargin)); // 可显示的行数
        mBufferBegin = mBufferEnd = progress;
    }

    /**
     * 打开文件
     *
     * @param filePath file路径
     * @throws IOException
     */
    public void openBook(String filePath) throws IOException {
        mBookFile = new File(filePath);
        long length = mBookFile.length();
        mBookBufferLength = (int) length;
        mBookBuffer = new RandomAccessFile(mBookFile, "r").getChannel().map(FileChannel.MapMode.READ_ONLY, 0, length);
        mBookCharsetName = codeString(filePath);
    }


    /**
     * 判断文件的编码格式
     *
     * @param fileName :file
     * @return 文件编码格式
     */
    public static String codeString(String fileName) {
        BufferedInputStream bufferedInputStream;
        int fileCode = 0;
        String code;
        try {
            bufferedInputStream = new BufferedInputStream(new FileInputStream(fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            bufferedInputStream = null;
        }
        if (bufferedInputStream != null) {
            try {
                fileCode = (bufferedInputStream.read() << 8) + bufferedInputStream.read();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        switch (fileCode) {
            case 0xefbb:
                code = UTF_8;
                break;
            case 0xfffe:
                code = UNICODE;
                break;
            case 0xfeff:
                code = UTF_16_BE;
                break;
            default:
                code = GBK;
        }
        return code;
    }


    /**
     * 读取后一段落
     *
     * @return
     */
    protected byte[] readParagraphBack(int nextPosition) {
        int end = nextPosition;
        int i;
        byte b0, b1;
        if (mBookCharsetName.equals(UTF_16_LE)) {
            i = end - 2;
            while (i > 0) {
                b0 = mBookBuffer.get(i);
                b1 = mBookBuffer.get(i + 1);
                if (b0 == 0x0a && b1 == 0x00 && i != end - 2) {
                    i += 2;
                    break;
                }
                i--;
            }

        } else if (mBookCharsetName.equals(UTF_16_BE)) {
            i = end - 2;
            while (i > 0) {
                b0 = mBookBuffer.get(i);
                b1 = mBookBuffer.get(i + 1);
                if (b0 == 0x00 && b1 == 0x0a && i != end - 2) {
                    i += 2;
                    break;
                }
                i--;
            }
        } else {
            i = end - 1;
            while (i > 0) {
                b0 = mBookBuffer.get(i);
                if (b0 == 0x0a && i != end - 1) {
                    i++;
                    break;
                }
                i--;
            }
        }
        if (i < 0)
            i = 0;
        int nParaSize = end - i;
        int j;
        byte[] buf = new byte[nParaSize];
        for (j = 0; j < nParaSize; j++) {
            buf[j] = mBookBuffer.get(i + j);
        }
        return buf;
    }


    /**
     * 读取前一段落
     *
     * @param previousPosition
     * @return
     */
    protected byte[] readParagraphForward(int previousPosition) {
        int start = previousPosition;
        int i = start;
        byte b0, b1;
        // 根据编码格式判断换行
        if (mBookCharsetName.equals(UTF_16_LE)) {
            while (i < mBookBufferLength - 1) {
                b0 = mBookBuffer.get(i++);
                b1 = mBookBuffer.get(i++);
                if (b0 == 0x0a && b1 == 0x00) {
                    break;
                }
            }
        } else if (mBookCharsetName.equals(UTF_16_BE)) {
            while (i < mBookBufferLength - 1) {
                b0 = mBookBuffer.get(i++);
                b1 = mBookBuffer.get(i++);
                if (b0 == 0x00 && b1 == 0x0a) {
                    break;
                }
            }
        } else {
            while (i < mBookBufferLength) {
                b0 = mBookBuffer.get(i++);
                if (b0 == 0x0a) {
                    break;
                }
            }
        }
        int nParaSize = i - start;
        byte[] buf = new byte[nParaSize];
        for (i = 0; i < nParaSize; i++) {
            buf[i] = mBookBuffer.get(previousPosition + i);
        }
        return buf;
    }


    protected Vector<String> pageDown() {
        String strParagraph = "";
        Vector<String> lines = new Vector<>();
        // 一段一段的读取,一行一行的添加,一直到读取超过一页的行数。
        while (lines.size() < mPageLineCount && mBufferEnd < mBookBufferLength) {
            byte[] paragraphForward = readParagraphForward(mBufferEnd); // 读取一个段落
            mBufferEnd += paragraphForward.length;
            try {
                strParagraph = new String(paragraphForward, mBookCharsetName);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String strReturn = "";
            if (strParagraph.indexOf("\r\n") != -1) {
                strReturn = "\r\n";
                strParagraph = strParagraph.replaceAll("\r\n", "");
            } else if (strParagraph.indexOf("\n") != -1) {
                strReturn = "\n";
                strParagraph = strParagraph.replaceAll("\n", "");
            }

            if (strParagraph.length() == 0) {
                lines.add(strParagraph);
            }
            while (strParagraph.length() > 0) {
                // 计算一行的字数。
                int nSize = mPaint.breakText(strParagraph, true, mVisibleWidth, null);
                // 添加一行
                lines.add(strParagraph.substring(0, nSize));
                // 将段落减去这一行,变成剩下的一部分,除非超过一页,才会跳出
                strParagraph = strParagraph.substring(nSize);
                if (lines.size() >= mPageLineCount) {
                    break;
                }
            }
            if (strParagraph.length() != 0) {
                try {
                    //位置向后移
                    mBufferEnd -= (strParagraph + strReturn).getBytes(mBookCharsetName).length;
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
        return lines;
    }

    protected void pageUp() {
        if (mBufferBegin < 0)
            mBufferBegin = 0;
        Vector<String> lines = new Vector<>();
        String strParagraph = "";
        while (lines.size() < mPageLineCount && mBufferBegin > 0) {
            Vector<String> paraLines = new Vector<>();
            byte[] paraBuf = readParagraphBack(mBufferBegin);
            mBufferBegin -= paraBuf.length;
            try {
                strParagraph = new String(paraBuf, mBookCharsetName);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            strParagraph = strParagraph.replaceAll("\r\n", "");
            strParagraph = strParagraph.replaceAll("\n", "");

            if (strParagraph.length() == 0) {
                paraLines.add(strParagraph);
            }
            while (strParagraph.length() > 0) {
                int nSize = mPaint.breakText(strParagraph, true, mVisibleWidth,
                        null);
                paraLines.add(strParagraph.substring(0, nSize));
                strParagraph = strParagraph.substring(nSize);
            }
            lines.addAll(0, paraLines);
        }
        while (lines.size() > mPageLineCount) {
            try {
                mBufferBegin += lines.get(0).getBytes(mBookCharsetName).length;
                lines.remove(0);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        mBufferEnd = mBufferBegin;
        return;
    }

    protected void prePage() throws IOException {
        if (mBufferBegin <= 0) {
            mBufferBegin = 0;
            mIsFirstPage = true;
            return;
        } else
            mIsFirstPage = false;
        mIsLastPage = false;
        mLinesVector.clear();
        pageUp();
        mLinesVector = pageDown();
    }

    public void nextPage() throws IOException {
        if (mBufferEnd >= mBookBufferLength) {
            mIsLastPage = true;
            return;
        } else
            mIsLastPage = false;
        mIsFirstPage = false;
        mLinesVector.clear();
        mBufferBegin = mBufferEnd;
        mLinesVector = pageDown();
    }

    public void draw(Canvas canvas) {
        if (mLinesVector.size() == 0)
            mLinesVector = pageDown();
        if (mLinesVector.size() > 0) {
            if (mIsUserBg && mBookBgBitmap != null)
                canvas.drawBitmap(mBookBgBitmap, 0, 0, null);
            else {
                canvas.drawColor(mBackGroundColor);
            }
            int yHeight = mMarginHeight + mFontSize;
            // 一行行绘制文字
            for (String strLine : mLinesVector) {
                canvas.drawText(strLine, mMarginWidth, yHeight, mPaint);
                yHeight += mFontSize + mLineMargin;
            }
        }

        // 设置进度
        if (onProgressChangedListener!= null) {
            onProgressChangedListener.setProgress(mBufferBegin, mBookBufferLength);
        }
    }

    public void setBackground(Context context, int resourceID){
        if(context != null && resourceID != 0){
            mIsUserBg = true;
            setBgBitmap(BitmapFactory.decodeResource(context.getResources(),resourceID));
        }
    }
    public void setBgBitmap(Bitmap bitmap) {
        mBookBgBitmap = bitmap;

        Matrix matrix = new Matrix();
        int width = mBookBgBitmap.getWidth();// 获取资源位图的宽
        int height = mBookBgBitmap.getHeight();// 获取资源位图的高
        float w = (float) mWidth / (float) mBookBgBitmap.getWidth();
        float h = (float) mHeight / (float) mBookBgBitmap.getHeight();
        matrix.postScale(w, h);// 获取缩放比例
        mBookBgBitmap = Bitmap.createBitmap(mBookBgBitmap, 0, 0, width, height, matrix,true);// 根据缩放比例获取新的位图
    }

    public void setUseBg(boolean useBg) {
        mIsUserBg = useBg;
    }

    public boolean isFirstPage() {
        return mIsFirstPage;
    }

    public boolean isLastPage() {
        return mIsLastPage;
    }

    public int getEnd() {
        return mBufferEnd;
    }

    public float getProgress() {
        return (float) (mBufferBegin * 1.0 / mBookBufferLength);
    }

    public void setBookProgress(int progress) {
        if (progress < 0) {
            mBufferBegin = mBufferEnd = 0;
        } else if (progress > mBookBufferLength) {
            mBufferBegin = mBufferEnd = mBookBufferLength;
        } else {
            mBufferBegin = mBufferEnd = progress;
        }
        pageUp();
        if (mBufferBegin != 0) {
            mLinesVector.clear();
            mLinesVector = pageDown();

            mBufferBegin = mBufferEnd;
        }
        mLinesVector.clear();
        mLinesVector = pageDown();
    }

    public void setProgress(int progress) {

        if (progress < 0) {
            mBufferBegin = mBufferEnd = 0;
        } else if (progress > 100) {
            mBufferBegin = mBufferEnd = mBookBufferLength;
        } else {
            mBufferBegin = mBufferEnd = (int) (((float) progress / 100) * mBookBufferLength);
        }

        pageUp();
        if (mBufferBegin != 0) {
            mLinesVector.clear();
            mLinesVector = pageDown();

            mBufferBegin = mBufferEnd;
        }
        mLinesVector.clear();
        mLinesVector = pageDown();
    }

    public void setTextColor(int color) {
        mPaint.setColor(color);
    }

    /**
     * 设置接口
     *
     * @param onProgressChangedListener
     */
    public void setOnProgressChangedListener(OnProgressChangedListener onProgressChangedListener) {
        this.onProgressChangedListener = onProgressChangedListener;
    }

    /**
     * 设置阅读进度的接口
     */
    public interface OnProgressChangedListener {
        /**
         * 设置进度方法
         */
        void setProgress(int currentLength, int totalLength);
    }

    public String getCurrentPageContent(){
        return mLinesVector.toString();
    }
}
