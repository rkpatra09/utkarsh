package com.beroe.connectivity;

import com.jcraft.jsch.*;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SSHChannelImpl {
	private static final Logger LOGGER = Logger.getLogger(SSHChannelImpl.class
			.getName());
	private JSch jschSSHChannel;
	private String strUserName;
	private String strConnectionIP;
	private int intConnectionPort;
	private String strPassword;
	private Session sesConnection;
	private int intTimeOut;

	@SuppressWarnings("static-access")
	private void doCommonConstructorActions(String userName, String password,
			String connectionIP, String knownHostsFileName) {
		jschSSHChannel = new JSch();

		try {
			jschSSHChannel.setKnownHosts(knownHostsFileName);
			jschSSHChannel.setConfig("StrictHostKeyChecking", "no");

		} catch (JSchException jschX) {
			logError(jschX.getMessage());
		}

		strUserName = userName;
		strPassword = password;
		strConnectionIP = connectionIP;
	}

	public SSHChannelImpl(String userName, String password,
			String connectionIP, String knownHostsFileName) {
		doCommonConstructorActions(userName, password, connectionIP,
				knownHostsFileName);
		intConnectionPort = 22;
		intTimeOut = 60000;
	}

	public SSHChannelImpl(String userName, String password,
			String connectionIP, String knownHostsFileName, int connectionPort) {
		doCommonConstructorActions(userName, password, connectionIP,
				knownHostsFileName);
		intConnectionPort = connectionPort;
		intTimeOut = 60000;
	}

	public SSHChannelImpl(String userName, String password,
			String connectionIP, String knownHostsFileName, int connectionPort,
			int timeOutMilliseconds) {
		doCommonConstructorActions(userName, password, connectionIP,
				knownHostsFileName);
		intConnectionPort = connectionPort;
		intTimeOut = timeOutMilliseconds;
	}

	public String connect() {
		String errorMessage = null;

		try {
			sesConnection = jschSSHChannel.getSession(strUserName,
					strConnectionIP, intConnectionPort);
			sesConnection.setPassword(strPassword);
			java.util.Properties sshConfig = new java.util.Properties();
			sshConfig.put("StrictHostKeyChecking", "no");
			sesConnection.setConfig(sshConfig);
			sesConnection.connect(intTimeOut);
		} catch (JSchException jschX) {
			errorMessage = jschX.getMessage();
		}

		return errorMessage;
	}

	private String logError(String errorMessage) {
		if (errorMessage != null) {
			LOGGER.log(Level.SEVERE, "{0}:{1} - {2}", new Object[] {
					strConnectionIP, intConnectionPort, errorMessage });
		}

		return errorMessage;
	}

	private String logWarning(String warnMessage) {
		if (warnMessage != null) {
			LOGGER.log(Level.WARNING, "{0}:{1} - {2}", new Object[] {
					strConnectionIP, intConnectionPort, warnMessage });
		}

		return warnMessage;
	}

	public boolean sendCommand(String execCommand) throws SftpException,
			IOException {

		String[] commandType = execCommand.split(" +");
		System.out.println("Command = " + commandType);
		Boolean result = false;
		try {
			Channel channel = null;

			switch (commandType[0]) {
			case "mkdir":
				channel = sesConnection.openChannel("sftp");
				channel.connect();
				ChannelSftp c = (ChannelSftp) channel;
				String path = commandType[1].substring(0,
						commandType[1].lastIndexOf("/"));
				String directory = commandType[1].substring(
						commandType[1].lastIndexOf("/") + 1).trim();
				System.out.println("Path = " + path);
				System.out.println("Directory = " + directory);
				c.cd(path);
				String cpwd = c.pwd();
				System.out.println("pwd = " + cpwd);
				System.out.println("ls = " + c.ls(cpwd));
				// if cmd contain -p
				try {
					c.ls(directory);
				} catch (Exception e) {
					c.mkdir(directory);

				}
				//
				System.out.println("c.getExitStatus()" + c.getExitStatus());
				if (channel.getExitStatus() == -1
						|| channel.getExitStatus() == 0) {
					result = true;
				} else {
					result = false;
				}
				break;

			case "cat":
				channel = sesConnection.openChannel("exec");
				InputStream in = channel.getInputStream();
				((ChannelExec) channel).setCommand(execCommand);

				channel.connect();

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(in));
				String line;
				int index = 0;

				while ((line = reader.readLine()) != null) {
					System.out.println(++index + " : " + line);
				}

				System.out.println("COPY exit status "
						+ channel.getExitStatus());
				// System.out.println("Result " +out.toString());
				if (channel.getExitStatus() == -1
						|| channel.getExitStatus() == 0) {
					result = true;
				} else {
					result = false;
				}
				break;

			case "chmod":
				channel = sesConnection.openChannel("exec");
				((ChannelExec) channel).setCommand(execCommand);
				channel.connect();
				System.out.println("COPY exit status "
						+ channel.getExitStatus());
				if (channel.getExitStatus() == -1
						|| channel.getExitStatus() == 0) {
					result = true;
				} else {
					result = false;
				}
				break;

			case "cp":
				channel = sesConnection.openChannel("exec");
				((ChannelExec) channel).setCommand(execCommand);
				channel.connect();
				System.out.println("COPY exit status "
						+ channel.getExitStatus());
				if (channel.getExitStatus() == -1
						|| channel.getExitStatus() == 0) {
					result = true;
				} else {
					result = false;
				}
				break;

			case "sed":

				channel = sesConnection.openChannel("exec");
				((ChannelExec) channel).setCommand(execCommand);
				channel.setInputStream(null);
				((ChannelExec) channel).setErrStream(System.err);
				String str[] = commandType[1].split("/");
				System.out.println("String to be replaced = " + str[1]);
				System.out.println("String replaced = " + str[2]);
				File file = new File(commandType[2]);
				System.out.println("File = " + file.getName());
				if (channel.isClosed()) {
					break;
				}

			case "cd":

				channel = sesConnection.openChannel("exec");
				((ChannelExec) channel).setCommand(execCommand);
				channel.connect();
				System.out.println("COPY exit status "
						+ channel.getExitStatus());
				if (channel.getExitStatus() == -1
						|| channel.getExitStatus() == 0) {
					result = true;
				} else {
					result = false;
				}

				break;
			}

			channel.disconnect();
		} catch (JSchException jschX) {
			logWarning(jschX.getMessage());
			result = false;
		}
		return result;
	}

	public void close() {
		sesConnection.disconnect();
	}

	public String sendCommandresults(String execCommand, String commandOutput)
			throws SftpException, IOException {

		String[] commandType = execCommand.split(" +");
		System.out.println("Command = " + commandType);
		Boolean result = false;
		String line = null;
		BufferedReader reader = null;
		InputStream in = null;
		InputStreamReader inputStreamReader = null;
		try {
			Channel channel = null;

			switch (commandType[0]) {

			case "cat":
				channel = sesConnection.openChannel("exec");
				in = channel.getInputStream();
				((ChannelExec) channel).setCommand(execCommand);

				channel.connect();
				inputStreamReader = new InputStreamReader(in);
				reader = new BufferedReader(inputStreamReader);
				line = reader.readLine();
				System.out.println("commandOutput : " + line);
				System.out.println("COPY exit status "
						+ channel.getExitStatus());
				// System.out.println("Result " +out.toString());
				if (channel.getExitStatus() == -1
						|| channel.getExitStatus() == 0) {
					result = true;
				} else {
					result = false;
				}
				break;

			}
			channel.disconnect();
		} catch (JSchException jschX) {
			logWarning(jschX.getMessage());
			result = false;
		} finally {

			if (reader != null) {
				reader.close();
			}
			if (inputStreamReader != null) {
				inputStreamReader.close();
			}
			if (in != null) {
				in.close();
			}

		}
		return line;
	}

}