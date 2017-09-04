package BackupToDisk;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import SentEmail.SentEmail;

public class BackupToDisk
{
	public static void  main(String[] args) throws InterruptedException{
		System.out.println();
		Calendar now_star = Calendar.getInstance();
		SimpleDateFormat formatter_star = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		String day = formatter.format(now_star.getTime()); // 格式化后的日期
		System.out.println("程序开始时间: " + formatter_star.format(now_star.getTime()));
		System.out.println("===============================================");
		System.out.println("BackupToDisk.1.0.0");
		System.out.println("***********************************************");
		System.out.println();
		
		int args_len = args.length; // 系统传入主函数的参数长度
		String folder = null; // 指定计算目录
		String target = null; // 磁盘路径
		String source = null; // 备份源路径
		String backupOver = "./backupOver.txt";
		String allDiskDatabackupInfo = "./allDiskDatabackupInfo.txt";
		int disknum = 0;
		
		int logt = 0; // "-t"参数输入次数计算标志
		int logs = 0; // "-s"参数输入次数计算标志
		int logn = 0; // "-n"参数输入次数计算标志
		
		for (int len = 0; len < args_len; len += 2) {
			if (args[len].equals("-T") || args[len].equals("-t")) {
				target = args[len + 1];
				logt++;
			} else if (args[len].equals("-S") || args[len].equals("-s")) {
				source = args[len + 1];
				logs++;
			} else if (args[len].equals("-N") || args[len].equals("-n")) {
				disknum = Integer.valueOf(args[len + 1]);
				logn++;
			} else if ((args_len == 1) && args[0].equals("-help")) {
				System.out.println();
				System.out.println("Version: V1.0.0");
				System.out.println();
				System.out.println("Usage:\t java -jar BackupToDisk.jar [Options] [args...]");
				System.out.println();
				System.out.println("Options:");
				System.out.println("-help\t\t Obtain parameter description.");
				System.out.println(
						"-T or -t\t Set target path. The default value is null.");
				System.out.println(
						"-S or -s\t Set source path. The default value is null.");
				System.out.println(
						"-N or -n\t Set hard disk number. The default value is null.");
				System.out.println();
				return;
			} else {
				System.out.println();
				System.out.println("对不起，您输入的Options不存在，或者缺少所需参数，请参照以下参数提示输入！");
				System.out.println();
				System.out.println("Options:");
				System.out.println("-help\t\t Obtain parameter description.");
				System.out.println(
						"-T or -t\t Set target path. The default value is null.");
				System.out.println(
						"-S or -s\t Set source path. The default value is null.");
				System.out.println(
						"-N or -n\t Set hard disk number. The default value is null.");
				System.out.println();
				return;
			}
			if (logt > 1 || logs > 1 || logn > 1) {
				System.out.println();
				System.out.println("对不起，您输的入Options有重复，请参照以下参数提示输入！");
				System.out.println();
				System.out.println("Options:");
				System.out.println("-help\t\t Obtain parameter description.");
				System.out.println(
						"-T or -t\t Set target path. The default value is null.");
				System.out.println(
						"-S or -s\t Set source path. The default value is null.");
				System.out.println(
						"-N or -n\t Set hard disk number. The default value is null.");
				System.out.println();
				return;
			}
		}
		//my_mkdir("./Src_Data2/x10/outputdata/170806_E00454_0207_AH5Y3GCCXY");
		//String folder = "/Src_Data2/x10/outputdata/170806_E00454_0207_AH5Y3GCCXY";
		/*String FSize = folderSize(source);
		String diskResidualSpaceSize = diskResidualSpaceSize(target);
		if (FSize.equals("0")) {
			System.out.println(FSize+"G");
		} else {
			double foldersize = changeToGB(FSize);
			double disksize = changeToGB(diskResidualSpaceSize);
			System.out.println(foldersize+"G");
			System.out.println(disksize+"G");
			if (foldersize < disksize) {
				System.out.println("可备份");
			} else {
				System.out.println("不可备份");
			}
		}*/
		//target = "/Src_Data2/x10/outputdata/"; // 磁盘路径
		//System.out.println(md5sum("./rsync_run.py"));
		//String emailData = "邮件正文！";
		/*try {
			SentEmail.sentEmail(mailTopic, emailData);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		String emailData = null;
		String mailTopic = null;
		int Input_length = 0;
		String InputArr[] = source.split("/");
		if (InputArr[InputArr.length - 1].equals("outputdata")) {
			//System.out.println(0);
			Input_length = 0;
		} else if (InputArr[InputArr.length - 2].equals("outputdata")) {
			//System.out.println(1);
			Input_length = 1;
		} else if (InputArr[InputArr.length - 3].equals("outputdata")) {
			//System.out.println(2);
			Input_length = 2;
		}
		
		String diskDataSourceInfofile = target + "/num-" + disknum + "-diskDataSourceInfo.txt";
		if (Input_length == 0) {
			for (File pathname : new File(source).listFiles()) {
				// 判断硬盘空间是否满足备份
				String FSize = folderSize(source);
				String diskResidualSpaceSize = diskResidualSpaceSize(target);
				if (FSize.equals("0")) {
					System.out.println(FSize+"G");
					continue;
				} else {
					double foldersize = changeToGB(FSize);
					double disksize = changeToGB(diskResidualSpaceSize);
					//System.out.println(foldersize+"G");
					//System.out.println(disksize+"G");
					if (foldersize < disksize) {
						//System.out.println("可备份");
						String dir_name = pathname.getName(); // 目录名
						ArrayList<String> BackupOver_file_list = readBackupOver(backupOver);
						if (!BackupOver_file_list.contains(dir_name)) {
							int re = backupFile(Input_length, pathname, target);
							if(re != 0) {
								emailData = source+"/"+dir_name+"备份出错，请检查原因！";
								mailTopic = "备份出错";
								try {
									SentEmail.sentEmail(mailTopic, emailData);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								System.out.println(source+"/"+dir_name+"备份出错，请检查原因！");
								break;
							} else {
								String Folder = pathname.getParent(); // 父目录
								backupOver(backupOver, dir_name); // 已备份列表
								diskDataSourceInfo(diskDataSourceInfofile, Folder+"/"+dir_name, disknum); //该硬盘数据源信息
								allDiskDatabackupInfo(allDiskDatabackupInfo, Folder+"/"+dir_name, disknum); //备份文件总信息
								//cpSampleInfoTxt(Folder+"/"+dir_name, target+"/"+dir_name); //备份文件改名信息文件
								rsyncFile (Folder+"/"+dir_name+"/"+dir_name+".SampleInfo.txt", target+"/"+dir_name);//备份文件改名信息文件
								emailData = Folder+"/"+dir_name+"已完成备份！";
								mailTopic = "备份完成";
								try {
									SentEmail.sentEmail(mailTopic, emailData);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								System.out.println(Folder+"/"+dir_name+"已完成！");
							}
						} else {
							continue;
						}
					} else {
						emailData = "硬盘空间不足，请更换硬盘!";
						mailTopic = "硬盘空间不足";
						try {
							SentEmail.sentEmail(mailTopic, emailData);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						System.out.println("由于硬盘空间不足，不可备份");
						return;
					}
				}
			}
			emailData = source+"目录下所有数据已备份完成！";
			mailTopic = "目录下所有数据已备份完成";
			try {
				SentEmail.sentEmail(mailTopic, emailData);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(source+"目录下所有数据已备份完成！");
		} else if (Input_length == 1) {
			/*int re = backupFile(Input_length, new File(source), target);
			if(re != 0) {
				System.out.println("备份出错");
			}*/
			// 判断硬盘空间是否满足备份
			String FSize = folderSize(source);
			String diskResidualSpaceSize = diskResidualSpaceSize(target);
			if (FSize.equals("0")) {
				System.out.println(FSize+"G");
				return;
			} else {
				double foldersize = changeToGB(FSize);
				double disksize = changeToGB(diskResidualSpaceSize);
				//System.out.println(foldersize+"G");
				//System.out.println(disksize+"G");
				if (foldersize < disksize) {
					//System.out.println("可备份");
					String dir_name = new File(source).getName(); // 目录名
					ArrayList<String> BackupOver_file_list = readBackupOver(backupOver);
					if (!BackupOver_file_list.contains(dir_name)) {
						int re = backupFile(Input_length, new File(source), target);
						if(re != 0) {
							emailData = source+"/"+dir_name+"备份出错，请检查原因！";
							mailTopic = "备份出错";
							try {
								SentEmail.sentEmail(mailTopic, emailData);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							System.out.println(source+"/"+dir_name+"备份出错，请检查原因！");
							return;
						} else {
							String Folder = new File(source).getParent(); // 父目录
							backupOver(backupOver, dir_name); // 已备份列表
							diskDataSourceInfo(diskDataSourceInfofile, Folder+"/"+dir_name, disknum); //该硬盘数据源信息
							allDiskDatabackupInfo(allDiskDatabackupInfo, Folder+"/"+dir_name, disknum); //备份文件总信息
							//cpSampleInfoTxt(Folder+"/"+dir_name, target+"/"+dir_name); //备份文件改名信息文件
							rsyncFile (Folder+"/"+dir_name+"/"+dir_name+".SampleInfo.txt", target+"/"+dir_name);//备份文件改名信息文件
							emailData = Folder+"/"+dir_name+"已完成备份！";
							mailTopic = "备份完成";
							try {
								SentEmail.sentEmail(mailTopic, emailData);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							System.out.println(Folder+"/"+dir_name+"已完成！");
						}
					}
				} else {
					emailData = "硬盘空间不足，请更换硬盘!";
					mailTopic = "硬盘空间不足";
					try {
						SentEmail.sentEmail(mailTopic, emailData);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("由于硬盘空间不足，不可备份");
					return;
				}
			}

		} else if (Input_length == 2) {
			/*int re = backupFile(Input_length, new File(source), target);
			if(re != 0) {
				System.out.println("备份出错");
			}*/
			// 判断硬盘空间是否满足备份
			String FSize = folderSize(source);
			String diskResidualSpaceSize = diskResidualSpaceSize(target);
			if (FSize.equals("0")) {
				System.out.println(FSize+"G");
				return;
			} else {
				double foldersize = changeToGB(FSize);
				double disksize = changeToGB(diskResidualSpaceSize);
				//System.out.println(foldersize+"G");
				//System.out.println(disksize+"G");
				if (foldersize < disksize) {
					//System.out.println("可备份");
					String dir_name = new File(source).getName(); // 目录名
					ArrayList<String> BackupOver_file_list = readBackupOver(backupOver);
					if (!BackupOver_file_list.contains(dir_name)) {
						int re = backupFile(Input_length, new File(source), target);
						if(re != 0) {
							emailData = source+"/"+dir_name+"备份出错，请检查原因！";
							mailTopic = "备份出错";
							try {
								SentEmail.sentEmail(mailTopic, emailData);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							System.out.println(source+"/"+dir_name+"备份出错，请检查原因！");
							return;
						} else {
							String Folder = new File(source).getParent(); // 父目录
							backupOver(backupOver, dir_name); // 已备份列表
							diskDataSourceInfo(diskDataSourceInfofile, Folder+"/"+dir_name, disknum); //该硬盘数据源信息
							allDiskDatabackupInfo(allDiskDatabackupInfo, Folder+"/"+dir_name, disknum); //备份文件总信息
							//cpSampleInfoTxt(Folder+"/"+dir_name, target+"/"+dir_name); //备份文件改名信息文件
							rsyncFile (Folder+"/"+dir_name+"/"+dir_name+".SampleInfo.txt", target+"/"+dir_name);//备份文件改名信息文件
							emailData = Folder+"/"+dir_name+"已完成备份！";
							mailTopic = "备份完成";
							try {
								SentEmail.sentEmail(mailTopic, emailData);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							System.out.println(Folder+"/"+dir_name+"已完成！");
						}
					}
				} else {
					emailData = "硬盘空间不足，请更换硬盘!";
					mailTopic = "硬盘空间不足";
					try {
						SentEmail.sentEmail(mailTopic, emailData);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println("由于硬盘空间不足，不可备份");
					return;
				}
			}
		} else {
			System.out.println(source + "是非法输入，请重新输入！");
			return;
		}

		/*String diskResidualSpaceSize = diskResidualSpaceSize(disk);
		if (diskResidualSpaceSize.equals("0")) {
			System.out.println(diskResidualSpaceSize+"G");
		} else {
			double foldersize = changeToGB(diskResidualSpaceSize);
			System.out.println(foldersize+"G");
		}*/
	
		Calendar now_end = Calendar.getInstance();
		SimpleDateFormat formatter_end = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println();
		System.out.println("==============================================");
		System.out.println("程序结束时间: " + formatter_end.format(now_end.getTime()));
		System.out.println();
	}
	
	// 备份文件改名信息文件
	public static void cpSampleInfoTxt(String source, String target){
		String cmd = "cp " + source + "/*.SampleInfo.txt " + target;
		System.out.println(cmd);
		try {
			Process process = Runtime.getRuntime().exec(cmd);
			BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = null;
			while ((line = input.readLine()) != null) {	
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 该硬盘的数据来源信息列表文件
	public static void diskDataSourceInfo(String outputfile, String Source, int disknum){
		File file = new File(outputfile);
		int log = 0;
		if (file.isFile() && file.exists()) { // 判断文件是否存在
			log = 1;
			//System.out.println("文件存在！");
		}
		// 写到输出文件
		try {
			FileWriter fw = new FileWriter(outputfile,true);
			BufferedWriter bw = new BufferedWriter(fw);
			// 如果文件不存在，则创建
			if (log==0) {
				bw.write("硬盘号："+"\t"+disknum+"号盘"+"\r\n");
			}
			bw.write(Source+ "\r\n");
			bw.close();
			fw.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// 该硬盘的数据来源信息列表文件
	public static void allDiskDatabackupInfo(String outputfile, String Source, int disknum){
		// 写到输出文件
		try {
			FileWriter fw = new FileWriter(outputfile,true);
			BufferedWriter bw = new BufferedWriter(fw);
			File file = new File(outputfile);
			bw.write(Source+"\t"+disknum+"号硬盘"+"\r\n");
			bw.close();
			fw.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// 读已备份的名单文件
	public static ArrayList<String> readBackupOver(String filePath)
	{
		ArrayList<String> BackupOver_file_list = new ArrayList<String>(); // 需要备份的文件列表
		try {
			String encoding = "GBK";
			File file = new File(filePath);

			if (file.isFile() && file.exists()) { // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding); // 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;

				while ((lineTxt = bufferedReader.readLine()) != null) {
					BackupOver_file_list.add(lineTxt);
				}
				read.close();
			} else {
				System.out.println();
				System.out.println(filePath + "文件不存在");
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错：" + filePath);
			e.printStackTrace();
		}
		return BackupOver_file_list;
	}
	
	// 新建数据信息文件
	public static void backupOver(String outputfile, String folder){
		// 写到输出文件
		try {
			FileWriter fw = new FileWriter(outputfile,true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(folder+ "\r\n");
			bw.close();
			fw.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// 判断目录大小
	public static String folderSize (String folder) {
		String foldersize = null;
		String cmd = "du -h "+folder;
		String data = null;
		try {
			Process process = Runtime.getRuntime().exec(cmd);
			BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = null;
			// 循环读出系统返回数据，保证系统调用已经正常结束
			while ((line = input.readLine()) != null) {
				data = line;
				//System.out.println(line);
			}
			String dataArr[] = data.split("\t");
			foldersize = dataArr[0];			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return foldersize;
	}
	
	// 判断磁盘剩余空间
	public static String diskResidualSpaceSize (String disk) {
		String foldersize = null;
		String cmd = "df -h";
		try {
			Process process = Runtime.getRuntime().exec(cmd);
			BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = null;
			int log = 0;
			int Avail = 0;
			// 循环读出系统返回数据，保证系统调用已经正常结束
			while ((line = input.readLine()) != null) {
				if (log == 0) {
					log++;
					continue;
				} else {
					log++;
					String dataArr[] = line.split(" ");
					if (dataArr[dataArr.length-1].equals(disk) || (dataArr[dataArr.length-1]+"/").equals(disk)) {
						for (int i=0; i<dataArr.length; i++) {
							if(dataArr[i].equals(" ") || dataArr[i].equals("") || dataArr[i]==null) {
								continue;
							} else {
								if (Avail==3) {
									foldersize = dataArr[i];
									break;
								}
								Avail++;
							}
						}
						break;
					}
				}				
			}				
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return foldersize;
	}
	
	// 把容量单位转换为GB
	public static double changeToGB(String SpaceSize){
		String size = SpaceSize.substring(0,SpaceSize.length()-1);			
		String unit = SpaceSize.substring(SpaceSize.length()-1,SpaceSize.length());
		//System.out.println(SpaceSize);
		double foldersize = 0;
		if (size.split("\\.").length==1) {
			if (unit.equals("T")) {
				foldersize = Integer.parseInt(size)*1024.0;
			} else if (unit.equals("G")) {
				foldersize = Integer.parseInt(size);
			} else if (unit.equals("M")) {
				foldersize = Integer.parseInt(size)/1024.0;
			} else if (unit.equals("K")) {
				foldersize = 0;
			}
			//System.out.println(foldersize+"G");
		} else {
			if (unit.equals("T")) {
				foldersize = Double.parseDouble(size)*1024.0;
			} else if (unit.equals("G")) {
				foldersize = Double.parseDouble(size);
			} else if (unit.equals("M")) {
				foldersize = Double.parseDouble(size)/1024.0;
			} else if (unit.equals("K")) {
				foldersize = 0;
			}
			//System.out.println(foldersize+"G");
		}
		return foldersize;
	}
	
	/**
	 * 调用正则表达式的方法。
	 * 
	 * @param str
	 * @param regEx
	 * @return String
	 */
	public static String Regular_Expression(String str, String regEx)
	{
		String data = null;
		// 编译正则表达式
		Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(str);
		if (matcher.find()) {
			data = matcher.group();
		}
		return data;
	}
	
	// 开始备份
	public static int backupFile(int inputlenght, File pathname, String targetFolder) {
		if (inputlenght == 0 || inputlenght == 1) {
			if (pathname.isDirectory()) { // 如果是目录
				String dir_name = pathname.getName(); // 目录名
				String InputArr[] = dir_name.split("_");
				if (InputArr.length == 4) {
					if(InputArr[0].length() == 6 && Regular_Expression(InputArr[0], "^[0-2]\\d[0-1]\\d[0-3]\\d") != null){
						if(Regular_Expression(InputArr[1], "^[A-Z]{1,}\\d{1,}") != null){
							if(InputArr[2].length() == 4 && Regular_Expression(InputArr[2], "^\\d{4}") != null){
								if(Regular_Expression(InputArr[3], "^[A-Z0-9]{1,}") != null){
									
									for (File porject : pathname.listFiles()) {
										if (porject.isDirectory()) { // 如果是目录
											// 获取文件的绝对路径
											String Folder = porject.getParent(); // 父目录
											String Por_name = porject.getName(); // 子目录名
											//System.out.println(inputlenght+"\t"+Folder+"\t"+Por_name);
											
											int re = backupWorkFun(Folder, dir_name, targetFolder);
											if(re != 0) {
												return 1;
											}
										}
									}
									
								} else {
									return 0;
								}
							} else {
								return 0;
							}
						} else {
							return 0;
						}
					} else {
						return 0;
					}
				} else {
					return 0;
				}
			}
		} else if (inputlenght == 2) {
			// 获取文件的绝对路径
			String Folder = pathname.getParent(); // 父目录
			String Foldername = new File(Folder).getName(); // 父目录名
			String Por_name = pathname.getName(); // 子目录名
			
			int re = backupWorkFun(Folder, Foldername, targetFolder);
			if(re != 0) {
				return 1;
			}
			//System.out.println(inputlenght+"\t"+Folder+"\t"+Foldername+"\t"+Por_name);
		}
		return 0;
	}
	
	// 创建目录的方法。
	public static void my_mkdir(String dir_name)
	{
		File file = new File(dir_name);
		// 如果文件不存在，则创建
		if (!file.exists() && !file.isDirectory()) {
			file.mkdirs();
		}
	}
	
	// 获取需要备份的文件列表
	public static ArrayList<String> findBackupFile(String backupFolder){
		ArrayList<String> Backup_file_list = new ArrayList<String>(); // 需要备份的文件列表
		String cmd = "find "+ backupFolder + " -type f -name *fastq.gz";
		try {
			Process process = Runtime.getRuntime().exec(cmd);
			BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = null;
			// 循环读出系统返回数据，保证系统调用已经正常结束
			while ((line = input.readLine()) != null) {
				//System.out.println(line);
				Backup_file_list.add(line);
			}			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Backup_file_list;
	}
	
	// 获取文件md5
	public static String md5sum(String file)
	{
		String cmd = "md5sum " + file;
		String data = null;
		try {
			Process process = Runtime.getRuntime().exec(cmd);
			BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = null;
			while ((line = input.readLine()) != null) {
				String InputArr[] = line.split(" ");
				data = InputArr[0];
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}
	
	// rsync文件
	public static void rsyncFile (String sourceFile, String targetFolder) {
		File file = new File(sourceFile);
		// 获取文件的绝对路径
		String Folder = file.getParent();
		// 文件名
		String FileName = file.getName();
		String cmd = "rsync -aP --no-links --include='" + FileName + "' --include='*/'  --exclude='*'  " + Folder + "/ "+ targetFolder+"/";
		//System.out.println(cmd);
		try {
			Process process = Runtime.getRuntime().exec(cmd);
			BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = null;
			while ((line = input.readLine()) != null) {
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 新建数据信息文件
	public static void newDataInfoFile(String outputfile){
		// 写到输出文件
		try {
			FileWriter fw = new FileWriter(outputfile);
			BufferedWriter bw = new BufferedWriter(fw);
			//bw.write("Total size:" + "\t" + folderSize + "\t" + backupFolder + "\r\n");
			bw.write("Path" + "\t" + "FileName" + "\t" + "Size"+ "\t" + "md5"+ "\r\n");
			bw.close();
			fw.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// 向数据信息文件添加数据信息
	public static void addDataInfoToFile(String file, String Folder, String FileName, String FileSize, String md5){
		int x = 0;
		while (true) {
			// 写到输出文件
			try {
				FileWriter fw = new FileWriter(file,true);
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(Folder + "\t" + FileName + "\t" + FileSize + "\t" + md5+ "\r\n");
				bw.close();
				fw.close();
				if (x != 0) {
					System.out.println();
					System.out.println("ssh过程中拋出异常，但程序已自动修复成功！ ");
					x = 0;
				}
				break;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				x++;
			}
			if (x == 100) {
				System.out.println();
				System.out.println("第"+x+"次创建信息文件异常!");
				return;
			} else {
				System.out.println();
				System.out.println("第"+x+"次创建信息文件异常!");
				continue;
			}	
		}
	}
	
	// 进行备份工作
	public static int backupWorkFun(String backupFolder, String folderName, String targetFolder){
		String sourceFolderSize = folderSize (backupFolder); // 获取目录所有文件总大小
		my_mkdir(targetFolder+"/"+folderName); // 在备份硬盘创建目录
		String sourcefile = backupFolder + "/" + folderName + "-All-File-Info.txt";
		String targetfile = targetFolder+"/"+folderName + "/" + folderName + "-All-File-Info.txt";
		newDataInfoFile(sourcefile);
		newDataInfoFile(targetfile);

		ArrayList<String> Backup_file_list = findBackupFile(backupFolder); // 需要备份的文件列表
		for(int i=0; i<Backup_file_list.size(); i++){
			
			File file = new File(Backup_file_list.get(i));
			// 获取源文件的绝对路径
			String Folder = file.getParent();
			// 源文件名
			String FileName = file.getName();
			
			String sourceArr[] = Backup_file_list.get(i).split("/");
			int log = 0;
			String dir = ""; // 备份文件目录结构
			for(int j=0; j<sourceArr.length-2; j++){
				if (sourceArr[j].equals(folderName) || log == 1) {
					dir += "/"+sourceArr[j+1];
					log = 1;
				} else {
					continue;
				}
			}
			//System.out.println(dir);
			String backupfilepath = targetFolder+"/"+folderName+dir; //备份文件目录
			my_mkdir(backupfilepath); // 在备份硬盘创建目录
			
			rsyncFile(Backup_file_list.get(i), backupfilepath); // rsync文件
			
			String FileSize = folderSize (Backup_file_list.get(i)); // 获取源文件大小
			String sourcefilemd5 = md5sum(Backup_file_list.get(i));
			//System.out.println(Backup_file_list.get(i));
			
			String targetFileSize = folderSize (backupfilepath+"/"+FileName); // 获取硬盘文件大小
			String targetfilemd5 = md5sum(backupfilepath+"/"+FileName);
			//System.out.println(backupfilepath+"/"+FileName);
			
			//System.out.println(sourcefilemd5+"\t"+targetfilemd5);
			
			// 对比md5
			if (sourcefilemd5.equals(targetfilemd5)) {
				addDataInfoToFile(sourcefile, Folder, FileName, FileSize, sourcefilemd5);
				addDataInfoToFile(targetfile, "/"+folderName+dir, FileName, targetFileSize, targetfilemd5);
				//System.out.println("源文件与备份文件md5对比一致！");
			} else {
				System.out.println("源文件与备份文件md5对比不一致，请检查原因！");
				return 1;
			}

		}
		
		String targetFolderSize = folderSize (targetFolder+"/"+folderName); // 获取目录所有文件总大小
		addDataInfoToFile(sourcefile, "Total size:", sourceFolderSize, backupFolder, "");
		addDataInfoToFile(targetfile, "Total size:", targetFolderSize, "/"+folderName, "");
		
		return 0;
	}
	
}

