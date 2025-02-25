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

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.squashfs.compression.ZstdCompression;
import de.topobyte.squashfs.tools.SquashConvertDirectory;

public class AppimageCreator
{

	final static Logger logger = LoggerFactory.getLogger(AppimageCreator.class);

	private Path appImageDir;
	private Path fileAppImage;

	public AppimageCreator(Path appImageDir, Path fileAppImage)
	{
		this.appImageDir = appImageDir;
		this.fileAppImage = fileAppImage;
	}

	public void execute() throws IOException
	{
		Path filePreImage = Files.createTempFile("appimage", null);
		filePreImage.toFile().deleteOnExit();

		logger.info("Input: {}", appImageDir);
		logger.info("Output: {}", fileAppImage);
		logger.info("Temporary squash fs: {}", filePreImage);

		// Download appimage runtime
		String urlRuntime = "https://github.com/AppImage/type2-runtime/releases/download/continuous/runtime-x86_64";
		URLConnection connection = new URL(urlRuntime).openConnection();
		byte[] dataRuntime = IOUtils.toByteArray(connection.getInputStream());
		logger.info("Size of appimage runtime: {}", dataRuntime.length);

		// Create squashfs filesystem for input directory at offset so that we
		// leave empty space for the appimage runtime.
		SquashConvertDirectory task = new SquashConvertDirectory();
		task.convertToSquashFs(appImageDir, filePreImage,
				new ZstdCompression(8), dataRuntime.length);

		// Replace target file with the one we just created
		Files.copy(filePreImage, fileAppImage,
				StandardCopyOption.REPLACE_EXISTING);

		// Prepend runtime
		OutputStream os = Files.newOutputStream(fileAppImage,
				StandardOpenOption.WRITE);
		os.write(dataRuntime);
		os.close();

		// Set executable flag on the target file
		fileAppImage.toFile().setExecutable(true);
	}

}
