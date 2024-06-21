package com.swmaestro.shorts.domain;

import java.io.*;

public class FileSet {
    private final File tempInput;
    private final File tempSubtitle;
    private final File tempOutput;

    public FileSet(final long id, final String inputSuffix) throws Exception {
        this.tempInput = File.createTempFile("input" + id, "." + inputSuffix);
        this.tempSubtitle = File.createTempFile(String.valueOf(id), ".srt");
        this.tempOutput = File.createTempFile(String.valueOf(id), ".mp4");
    }

    public void copyToInput(final InputStream in) throws Exception {
        final OutputStream out = new FileOutputStream(tempInput);
        transferStream(in, out);
    }

    public void copyToSubtitle(final InputStream in) throws Exception {
        final OutputStream out = new FileOutputStream(tempSubtitle);
        transferStream(in, out);
    }

    public String getInputPath() {
        return tempInput.getAbsolutePath();
    }

    public String getSubtitlePath() {
        final String initialPath = tempSubtitle.getAbsolutePath();
        final String replacedPath = initialPath.replace('\\', '/').replaceFirst("C", "C\\\\\\\\");
        return replacedPath;
    }

    public String getOutputPath() {
        return tempOutput.getAbsolutePath();
    }

    public String getOutputName() {
        return tempOutput.getName();
    }

    public void deleteAll() {
        tempInput.delete();
        tempSubtitle.delete();
        tempOutput.delete();
    }

    private void transferStream(final InputStream in, final OutputStream out) throws Exception {
        final BufferedInputStream bin = new BufferedInputStream(in);
        final BufferedOutputStream bout = new BufferedOutputStream(out);
        final byte[] buffer = new byte[2048];
        int bytes;
        while ((bytes = bin.read(buffer)) != -1) {
            bout.write(buffer, 0, bytes);
        }
        bout.flush();
        out.close();
        bin.close();
        bout.close();
    }
}
