package com.capgemini.bankapp.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.RollingFileAppender;

import com.capgemini.bankapp.exception.BankAccountNotFoundException;
import com.capgemini.bankapp.exception.LowBalanceException;
import com.capgemini.bankapp.model.BankAccount;
import com.capgemini.bankapp.service.BankAccountService;
import com.capgemini.bankapp.service.impl.BankAccountServiceImpl;

public class BankAccountClient {

	static final Logger logger = Logger.getLogger(BankAccountClient.class);

	public static void main(String[] args) throws BankAccountNotFoundException {

		int choice;
		int accountId;
		int recepientAccountId;
		String accountHolderName;
		String accountType;
		double balance;
		double accountBalance;
		double amountToBeWithdrawn;
		double amountToBeDeposited;
		double amountToBeTransferred;
		BankAccountService bankService = new BankAccountServiceImpl();

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
			while (true) {
				System.out.println("1. Add New BankAccount\n2. Withdraw\n3. Deposit\n4. Fund Transfer");
				System.out.println("5. Delete BankAccount\n6. Display All BankAccount Details");
				System.out.println("7. Search BankAccount\n8. Check Balance\n9. Update Account\n 10. Exit\n");
				System.out.print("Please enter your choice = ");
				choice = Integer.parseInt(reader.readLine());

				switch (choice) {

				case 1:
					System.out.println("Enter account holder name: ");
					accountHolderName = reader.readLine();
					System.out.println("Enter account type: ");
					accountType = reader.readLine();
					System.out.println("Enter account balance: ");
					accountBalance = Double.parseDouble(reader.readLine());
					BankAccount account = new BankAccount(accountHolderName, accountType, accountBalance);
					if (bankService.addNewBankAccount(account))
						System.out.println("Account created successfully...\n");
					else
						System.out.println("failed to create new account...\n");
					break;

				case 2:
					System.out.println("Enter account ID: ");
					accountId = Integer.parseInt(reader.readLine());
					System.out.println("Enter amount to be withdrawn: ");
					amountToBeWithdrawn = Double.parseDouble(reader.readLine());
					try {
						accountBalance = bankService.withdraw(accountId, amountToBeWithdrawn);
						System.out.println("   Transcation successful :" + accountBalance);
					} catch (LowBalanceException e) {
						logger.error("Withdraw failed", e);
					}
					break;

				case 3:
					System.out.println("Enter account ID: ");
					accountId = Integer.parseInt(reader.readLine());
					System.out.println("Enter amount to be Deposited: ");
					amountToBeDeposited = Double.parseDouble(reader.readLine());
					accountBalance = bankService.deposit(accountId, amountToBeDeposited);
					System.out.println("   Transcation successful :" + accountBalance);
					break;

				case 4:
					System.out.println("---Fund Transfer---");
					System.out.println("Enter account ID of the sender: ");
					accountId = Integer.parseInt(reader.readLine());
					System.out.println("Enter account ID of the receiver: ");
					recepientAccountId = Integer.parseInt(reader.readLine());
					System.out.println("Enter amount to be Transferred: ");
					amountToBeTransferred = Double.parseDouble(reader.readLine());
					try {
						accountBalance = bankService.fundTransfer(accountId, recepientAccountId, amountToBeTransferred);
						System.out.println("   ---Transcation successful-- \n --New Balance =" + accountBalance + "--");
					} catch (LowBalanceException e) {

						// System.out.println(e.getMessage());
						logger.error(e);
					}
					break;

				case 5:
					System.out.println("---Delete bank account---");
					System.out.println("Enter account ID of the account to be deleted:");
					accountId = Integer.parseInt(reader.readLine());
					if (bankService.deleteBankAccount(accountId)) {
						System.out.println("---Bank Account deleted---");
					} else {
						System.out.println("---Account not Found---");
					}
					break;

				case 6:
					System.out.println("---Display All BankAccount Details---");
					List<BankAccount> accounts = bankService.findAllBankAccounts();
					for (BankAccount bankAccount : accounts) {
						System.out.println(bankAccount);
					}
					break;

				case 7:

					System.out.println("---Search BankAccount---");
					System.out.println("Enter account ID of the account to be searched:");
					accountId = Integer.parseInt(reader.readLine());
					
					try {
						BankAccount bankAccount =	bankService.searchBankAccount(accountId);
						System.out.println(bankAccount);
					}
					
					catch(BankAccountNotFoundException e) {
						logger.error("Account not found",e);
					}
					
				
					break;

				case 8:

					System.out.println("---Check Balance---");
					System.out.println("Enter account ID of the account to be checked:");
					accountId = Integer.parseInt(reader.readLine());
					accountBalance = bankService.checkBalance(accountId);
					System.out.println("   ---Transcation successful-- \n --New Balance =" + accountBalance + "--");

					break;

				case 9:
					System.out.println("---Update the Bank Account Details---");
					System.out.println("Enter account ID of the account to be updated:");
					accountId = Integer.parseInt(reader.readLine());
					try {
						bankService.searchBankAccount(accountId);
					}
					catch(BankAccountNotFoundException e) {
						logger.error("Account not found",e);
					}
					
					break;
				case 10:
					System.out.println("Thanks for banking with us.");
					System.exit(0);
				}
			}
			}
				catch(IOException e)
					{
							//			e.printStackTrace();
						logger.error(e);
					}
	}
}
