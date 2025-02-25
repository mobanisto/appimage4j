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
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.topobyte.utilities.apache.commons.cli.OptionHelper;
import de.topobyte.utilities.apache.commons.cli.commands.args.CommonsCliArguments;
import de.topobyte.utilities.apache.commons.cli.commands.options.CommonsCliExeOptions;
import de.topobyte.utilities.apache.commons.cli.commands.options.ExeOptions;
import de.topobyte.utilities.apache.commons.cli.commands.options.ExeOptionsFactory;

public class ExtractAppimage
{

	final static Logger logger = LoggerFactory
			.getLogger(AppimageExtractor.class);

	private static final String OPTION_INPUT = "input";
	private static final String OPTION_OUTPUT = "output";

	public static ExeOptionsFactory OPTIONS_FACTORY = new ExeOptionsFactory() {

		@Override
		public ExeOptions createOptions()
		{
			Options options = new Options();
			// @formatter:off
			OptionHelper.addL(options, OPTION_INPUT, true, true, "file", "app image executable");
			OptionHelper.addL(options, OPTION_OUTPUT, true, true, "directory", "extraction directory");
			// @formatter:on
			return new CommonsCliExeOptions(options, "[options]");
		}

	};

	public static void main(String name, CommonsCliArguments arguments)
			throws IOException
	{
		CommandLine line = arguments.getLine();
		String input = line.getOptionValue(OPTION_INPUT);
		String output = line.getOptionValue(OPTION_OUTPUT);

		Path fileAppImage = Paths.get(input);
		Path appImageDir = Paths.get(output);

		AppimageExtractor task = new AppimageExtractor(fileAppImage,
				appImageDir);
		task.execute();
	}

}
