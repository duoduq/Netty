/*
 * JBoss, Home of Professional Open Source
 *
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
 * by the @author tags. See the COPYRIGHT.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package net.gleamynode.netty.handler.codec.string;

import static net.gleamynode.netty.channel.Channels.*;

import java.nio.charset.Charset;

import net.gleamynode.netty.buffer.ChannelBuffers;
import net.gleamynode.netty.channel.ChannelDownstreamHandler;
import net.gleamynode.netty.channel.ChannelEvent;
import net.gleamynode.netty.channel.ChannelHandlerContext;
import net.gleamynode.netty.channel.ChannelPipelineCoverage;
import net.gleamynode.netty.channel.MessageEvent;

/**
 * @author The Netty Project (netty-dev@lists.jboss.org)
 * @author Trustin Lee (tlee@redhat.com)
 *
 * @version $Rev:231 $, $Date:2008-06-12 16:44:50 +0900 (목, 12 6월 2008) $
 *
 */
@ChannelPipelineCoverage("all")
public class StringEncoder implements ChannelDownstreamHandler {

    private final String charsetName;

    public StringEncoder() {
        this(Charset.defaultCharset());
    }

    public StringEncoder(String charsetName) {
        this(Charset.forName(charsetName));
    }

    public StringEncoder(Charset charset) {
        if (charset == null) {
            throw new NullPointerException("charset");
        }
        charsetName = charset.name();
    }

    public void handleDownstream(
            ChannelHandlerContext context, ChannelEvent evt) throws Exception {
        if (!(evt instanceof MessageEvent)) {
            context.sendDownstream(evt);
            return;
        }

        MessageEvent e = (MessageEvent) evt;
        if (!(e.getMessage() instanceof String)) {
            context.sendDownstream(evt);
            return;
        }

        String src = (String) e.getMessage();
        write(context, e.getChannel(), e.getFuture(), ChannelBuffers.wrappedBuffer(src.getBytes(charsetName)));
    }
}
