// Copyright 2025 Mobanisto UG (haftungsbeschränkt)
//
// This file is part of appimage4j.
//
// appimage4j is free software: you can redistribute it and/or modify
// it under the terms of the GNU Lesser General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// appimage4j is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General Public License
// along with appimage4j. If not, see <http://www.gnu.org/licenses/>.

package de.mobanisto.appimage4j;

import static java.lang.System.lineSeparator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.squashfs.SquashFsReader;
import de.topobyte.squashfs.tools.SquashExtract;
import de.topobyte.squashfs.tools.SquashFsReaderUtil;
import net.fornwall.jelf.ElfFile;

public class AppimageExtractor
{

	final static Logger logger = LoggerFactory
			.getLogger(AppimageExtractor.class);

	private Path fileAppImage;
	private Path appImageDir;

	public AppimageExtractor(Path fileAppImage, Path appImageDir)
	{
		this.fileAppImage = fileAppImage;
		this.appImageDir = appImageDir;
	}

	public void execute() throws IOException
	{
		byte[] data = IOUtils.toByteArray(Files.newInputStream(fileAppImage));

		// Determine size of embedded executable
		ElfFile elf = ElfFile.from(data);
		long elfSize = elf.e_shoff + elf.e_shentsize * elf.e_shnum;

		// Extract using squashfs-tools
		SquashExtract task = new SquashExtract();
		try (SquashFsReader reader = SquashFsReaderUtil
				.createReader(fileAppImage, (int) elfSize, false)) {
			logger.info(lineSeparator() + reader.getSuperBlock());
			logger.info(lineSeparator() + reader.getIdTable());
			logger.info(lineSeparator() + reader.getFragmentTable());
			logger.info(lineSeparator() + reader.getExportTable());

			task.extract(reader, appImageDir);
		}
	}

}
