/****************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one   *
 * or more contributor license agreements.  See the NOTICE file *
 * distributed with this work for additional information        *
 * regarding copyright ownership.  The ASF licenses this file   *
 * to you under the Apache License, Version 2.0 (the            *
 * "License"); you may not use this file except in compliance   *
 * with the License.  You may obtain a copy of the License at   *
 *                                                              *
 *   http://www.apache.org/licenses/LICENSE-2.0                 *
 *                                                              *
 * Unless required by applicable law or agreed to in writing,   *
 * software distributed under the License is distributed on an  *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY       *
 * KIND, either express or implied.  See the License for the    *
 * specific language governing permissions and limitations      *
 * under the License.                                           *
 ****************************************************************/

package org.apache.james.mime4j.message;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;

class StringInputStream extends InputStream {

    private final CharsetEncoder encoder;
    private final CharBuffer cbuf;
    private final ByteBuffer bbuf;

    private int mark;

    StringInputStream(final CharSequence s, final Charset charset, int bufferSize) {
        super();
        this.encoder = charset.newEncoder()
            .onMalformedInput(CodingErrorAction.REPLACE)
            .onUnmappableCharacter(CodingErrorAction.REPLACE);
        this.bbuf = ByteBuffer.allocate(124);
        this.bbuf.flip();
        this.cbuf = CharBuffer.wrap(s);
        this.mark = -1;
    }

    StringInputStream(final CharSequence s, final Charset charset) {
        this(s, charset, 2048);
    }

    private void fillBuffer() throws IOException {
        this.bbuf.compact();
        CoderResult result = this.encoder.encode(this.cbuf, this.bbuf, true);
        if (result.isError()) {
            result.throwException();
        }
        this.bbuf.flip();
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if (b == null) {
            throw new NullPointerException("Byte array is null");
        }
        if (len < 0 || (off + len) > b.length) {
            throw new IndexOutOfBoundsException("Array Size=" + b.length +
                    ", offset=" + off + ", length=" + len);
        }
        if (!this.bbuf.hasRemaining() && !this.cbuf.hasRemaining()) {
            return -1;
        }
        int bytesRead = 0;
        while (len > 0) {
            if (this.bbuf.hasRemaining()) {
                int chunk = Math.min(this.bbuf.remaining(), len);
                this.bbuf.get(b, off, chunk);
                off += chunk;
                len -= chunk;
                bytesRead += chunk;
            } else {
                fillBuffer();
                if (!this.bbuf.hasRemaining() && !this.cbuf.hasRemaining()) {
                    break;
                }
            }
        }
        return bytesRead == 0 && !this.cbuf.hasRemaining() ? -1 : bytesRead;
    }

    @Override
    public int read() throws IOException {
        for (;;) {
            if (this.bbuf.hasRemaining()) {
                return this.bbuf.get() & 0xFF;
            } else {
                fillBuffer();
                if (!this.bbuf.hasRemaining() && !this.cbuf.hasRemaining()) {
                    return -1;
                }
            }
        }
    }

    @Override
    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    @Override
    public long skip(long n) throws IOException {
        int skipped = 0;
        while (n > 0 && this.cbuf.hasRemaining()) {
            this.cbuf.get();
            n--;
            skipped++;
        }
        return skipped;
    }

    @Override
    public int available() throws IOException {
        return this.cbuf.remaining();
    }

    @Override
    public void close() throws IOException {
    }

    @Override
    public void mark(int readlimit) {
        this.mark = this.cbuf.position();
    }

    @Override
    public void reset() throws IOException {
        if (this.mark != -1) {
            this.cbuf.position(this.mark);
            this.mark = -1;
        }
    }

    @Override
    public boolean markSupported() {
        return true;
    }

}
