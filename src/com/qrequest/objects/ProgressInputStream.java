package com.qrequest.objects;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;

public class ProgressInputStream extends InputStream {

	private BufferedInputStream inputStream;
	private int sumRead = 0;
	private Consumer<Integer> progessCallback;

	public ProgressInputStream(InputStream inputStream, int bufferSize, Consumer<Integer> progessCallback) {
		this.inputStream = new BufferedInputStream(inputStream, bufferSize);
		this.progessCallback = progessCallback;
	}

	@Override
	public int read(byte[] b) throws IOException {
		int readCount = inputStream.read(b);
		reportProgress(readCount);
		return readCount;
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		int readCount = inputStream.read(b, off, len);
		reportProgress(readCount);
		return readCount;
	}

	@Override
	public long skip(long n) throws IOException {
		long skip = inputStream.skip(n);
		reportProgress(skip);
		return skip;
	}

	@Override
	public int read() throws IOException {
		int read = inputStream.read();
		if (read != -1) {
			reportProgress(1);
		}
		return read;
	}

	private void reportProgress(long readCount){
		if (readCount!=-1) {
			sumRead+=readCount;
		}
		progessCallback.accept(sumRead);
	}
}