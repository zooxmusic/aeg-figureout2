package com.aeg.transfer.filter;

import com.jcraft.jsch.ChannelSftp;
import org.springframework.integration.file.filters.AbstractRegexPatternFileListFilter;
import org.springframework.integration.file.filters.FileListFilter;
import org.springframework.integration.file.filters.RegexPatternFileListFilter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by bszucs on 4/6/2016.
 */
public class XmlFileListFilter extends AbstractRegexPatternFileListFilter<ChannelSftp.LsEntry> {

    public XmlFileListFilter() {
        super(".xml");
    }

    @Override
    protected String getFilename(ChannelSftp.LsEntry file) {
        if(null == file) return "";
        return file.getFilename();
    }
}
