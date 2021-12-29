package com.qrequest.control;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;

public class ProcessInputStream extends InputStream {

    private BufferedInputStream inputStream;
    private int fileSize, 
	            sumRead = 0;
	private Consumer<Double> progessCallback;
    private double percent;

    public ProcessInputStream(InputStream inputStream, int bufferSize, int fileSize, Consumer<Double> progessCallback) {
        this.inputStream = new BufferedInputStream(inputStream, bufferSize);
        this.fileSize = fileSize;
		this.progessCallback = progessCallback;
    }

    @Override
    public int read(byte[] b) throws IOException {
        int readCount = inputStream.read(b);
        evaluatePercent(readCount);
        return readCount;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        int readCount = inputStream.read(b, off, len);
        evaluatePercent(readCount);
        return readCount;
    }

    @Override
    public long skip(long n) throws IOException {
        long skip = inputStream.skip(n);
        evaluatePercent(skip);
        return skip;
    }

    @Override
    public int read() throws IOException {
        int read = inputStream.read();
        if (read != -1) {
            evaluatePercent(1);
        }
        return read;
    }

    private void evaluatePercent(long readCount){
        if (readCount!=-1) {
            sumRead+=readCount;
            percent=sumRead*1.0 / fileSize;
        }
        progessCallback.accept(percent);
    }
}