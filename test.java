import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

class Account {
    public String ID;
    public String Name;
    private String PWD;
    private int Balance = 0;

    Account(String inID, String inPWD, String inName, int inBalance) {
        ID = inID;
        PWD = inPWD;
        Name = inName;
        Balance = inBalance;
    }
    String getPWD() {
        return PWD;
    }

    boolean setPWD(String inPWD) {
        if (inPWD.equals(""))
            return false;
        else {
            PWD = inPWD;
            return true;
        }
    }

    int getBalance() {
        return Balance;
    }

    /**
     * @param gAccount 輸入帳號
     * @param gMoney   輸入存款金額
     * @return if true, 存款成功; false, 輸入錯誤
     */
    boolean deposit(int inNum) {
        if (inNum > 0) {
            Balance += inNum;
            return true;
        } else
            return false;
    }

    /**
     * @param gAccount 輸入帳號
     * @param gMoney   輸入提款金額
     * @return if 1, 提款成功; if 2, 提款失敗, 金額小於0, if 3, 提款失敗，提款金額超過餘
     *         
     */
    int withdraw(int gMoney) {
        if (gMoney < 0)
            return 2; // 提款失敗，金額小於0
        else if (gMoney > Balance) {
            return 3; // 提款失敗，提款金額超過餘額
        } else { // 開始提款
            Balance -= gMoney; // 提錢,
            return 1;
        }
    }

    int login(String gID, String gPWD) {
        if (ID.equals(gID)) {
            if (PWD.equals(gPWD))
                return 1; // 登入成功
            else {
                return 2; // 密碼錯誤(登入錯誤)
            }
        } else {
            return 3; // 輸入帳號錯誤(登入錯誤)
        }
    }
}

class ATM {
    final int MaxDailyNum = 30000;
    public int InputOption = 0;
    private Account[] AccObj = new Account[10];
    private String FileName;
    
    ATM(String gFileName) {
        FileName = gFileName;
        loadFromFile();
    }

    void close() {
        saveToFile();
    }

    boolean deposit(Account gAccount, int gMoney) {
        boolean result = gAccount.deposit(gMoney);
        saveToFile();
        return result;
    }

    int withdraw(Account gAccount, int gMoney) {
        int result = gAccount.withdraw(gMoney);
        saveToFile();
        return result;
    }

    int getBalance(Account gAccount) {
        return gAccount.getBalance();
    }

    static String getOpenInfo() {
        String str = "營業時間: 8:00 AM -17:00 PM";
        return str;
    }

    void loadFromFile() {
        try {
            FileReader freader = new FileReader(FileName);
            BufferedReader breader = new BufferedReader(freader);
            int index = 0;
            breader.readLine(); // 忽略第一行
            while (breader.ready()) {
                String line = breader.readLine();
                String[] data = line.split(",");
                AccObj[index] = new Account(data[0], data[1], data[2], Integer.parseInt(data[3]));
                index++;
            }
            freader.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    void saveToFile() {
        /* TODO: 請寫出儲存結果回檔案的程式 */
        try {
            FileWriter fwriter = new FileWriter(FileName, false);
            BufferedWriter bwriter = new BufferedWriter(fwriter);
            bwriter.write("ID,PWD,NAME,Balance");
            bwriter.newLine();
            for (int i = 0; i < 3; i++) {
                /* TODO: 儲存結果 */
            	bwriter.write(AccObj[i].ID+","+AccObj[i].getPWD()+","+AccObj[i].Name+","+AccObj[i].getBalance());
                bwriter.newLine();
            }
            bwriter.flush();
            fwriter.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @param payer  付款人帳號
     * @param payee  收款人帳號
     * @param gMoney 轉帳金額
     * @return if true, 轉帳成功; false, 輸入錯誤
     */
    boolean transfer(Account payer, Account payee, int payMoney) {
        /* TODO: 請寫出轉帳程式 */
    	String pID1 = payer.ID;
    	String pID2 = payee.ID;
    	int i =0,temp=0;
    	for(i=0;i<3;i++) {
    		if(AccObj[i].ID.equals(pID1) == true) {
    			temp = withdraw(AccObj[i], payMoney);
    			if(temp == 1) {
    				withdraw(payer, payMoney);
    			}else{
    				return false;
    			}
    		}
    	}
    	for(i=0;i<3;i++) {
    		if(AccObj[i].ID.equals(pID2) == true) {
    			deposit(AccObj[i], payMoney);
    			deposit(payee, payMoney);
    		}
    	}
        return true;
    }

    /**
     * @param payer  付款人帳號
     * @param payee  收款人帳號
     * @param gMoney 轉帳金額
     * @return if 1:登入成功; 2:密碼錯誤; 3:輸入帳號錯誤
     */
    int login(Account gAccount, String gPWD) {
        /* TODO: 請寫出登入程式 */
    	String gID = gAccount.ID;
    	int i ; 	
    	for(i=0;i<3;i++) {
    		int status = AccObj[i].login(gID, gPWD);
    		if(status == 1) {
    			gAccount.Name = AccObj[i].Name;
    			gAccount.deposit(AccObj[i].getBalance());
    			return 1;
    			}
    		else if(status == 2)
    			return 2;
    	}
    	return 3;
    }
    int tryException(String szInput){
		try {		
			int gInt = Integer.parseInt(szInput); 					
			return gInt;
		} catch (Exception e) {
			return -1;
		}
	}
}

public class test {
    public static void main(String[] args) {
    	Scanner keyin = new Scanner(System.in);
        ATM myATM = new ATM("E:\\account.txt");
        String ID,PWD;
        String gID;

        int goATM,gmoney;
        System.out.println("請輸入付款人帳號:");
        ID=keyin.next();
        System.out.println("請輸入付款人密碼:");
        PWD=keyin.next();
        Account myAcc = new Account(ID, PWD, "", 0); 
        goATM = myATM.login(myAcc, PWD);
        if(goATM == 1) {
        	System.out.println("登入成功");
        	System.out.println("請輸入收款人帳號");
        	gID =keyin.next();
        	System.out.println("請輸入轉帳金額");
        	String szinput = keyin.next();
        	gmoney=myATM.tryException(szinput);
        	if(gmoney == -1) {
        		System.out.println("輸入金額錯誤");
        		return;
        	}
        	Account gAcc = new Account(gID, "", "",0);
        	goATM = myATM.login(gAcc,"");
        	if(goATM == 3) {
        		System.out.println("收款人帳號錯誤");
        	}
            else {
            	System.out.println("請輸入付款人密碼:");
                PWD=keyin.next();
                goATM = myATM.login(myAcc, PWD);
                if(goATM ==2) {
                	System.out.println("密碼錯誤");
                }else {
                	if(myATM.transfer(myAcc, gAcc, gmoney) == true)
                		myATM.saveToFile();
                	else {
                		System.out.println("付款人餘額不足");
                		System.out.println("付款人付款失敗");
                	}
                }
            }
        }else if(goATM ==2) {
        	System.out.println("付款人密碼錯誤");
        }else if(goATM ==3) {
        	System.out.println("付款人帳號錯誤");
        }
    }
}
