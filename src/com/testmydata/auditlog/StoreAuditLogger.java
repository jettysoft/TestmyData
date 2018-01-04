package com.testmydata.auditlog;

import java.util.List;

import org.apache.log4j.Logger;

import com.testmydata.binarybeans.StoreLogMappingBeanBinaryTrade;

/*This class contains all the methods required to access the database
 * It contains methods to store information in Database(AuditLog table )
 * It also contains methods to retrieve the tables separately.
 */

public class StoreAuditLogger {

	@SuppressWarnings("unused")
	private static Logger log = Logger.getLogger(StoreAuditLogger.class);

	/**
	 * 
	 * @param userId
	 * @param clientIpAddress
	 * @param deviceId
	 * @param attrNam
	 * @param oldValOfManagedAttr
	 * @param newValOfManagedAttr
	 * @param statusOfOperation
	 * @param description
	 */
	public static synchronized void logStoreTransaction(String userId, String moduleNam, String oldValOfManagedAttr,
			String newValOfManagedAttr, Boolean statusOfOperation, String description) {
		try {
			StoreLogMappingBeanBinaryTrade auditLogObj = new StoreLogMappingBeanBinaryTrade();
			auditLogObj.setUserId(userId);
			auditLogObj.setRole("");
			auditLogObj.setModuleName(moduleNam);
			auditLogObj.setOldValOfManagedAttr(oldValOfManagedAttr);
			auditLogObj.setNewValOfManagedAttr(newValOfManagedAttr);
			auditLogObj.setStatusOfOperation(statusOfOperation);
			auditLogObj.setDescription(description);

			// new DAO().createauditlogtable(auditLogObj);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}

	}

	/**
	 * 
	 * @param userId
	 * @param clientIpAddress
	 * @param deviceId
	 * @param moduleNam
	 * @param oldValOfManagedAttr
	 * @param newValOfManagedAttr
	 * @param statusOfOperation
	 * @param description
	 * @param role
	 */
	public static synchronized void logStoreTransaction(String userId, String moduleNam, String oldValOfManagedAttr,
			String newValOfManagedAttr, Boolean statusOfOperation, String description, String role) {

		try {
			StoreLogMappingBeanBinaryTrade auditLogObj = new StoreLogMappingBeanBinaryTrade();
			auditLogObj.setUserId(userId);
			auditLogObj.setRole(role);
			auditLogObj.setModuleName(moduleNam);
			auditLogObj.setOldValOfManagedAttr(oldValOfManagedAttr);
			auditLogObj.setNewValOfManagedAttr(newValOfManagedAttr);
			auditLogObj.setStatusOfOperation(statusOfOperation);
			auditLogObj.setDescription(description);
			// new DAO().createauditlogtable(auditLogObj);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}

	}

	/**
	 * 
	 * @param userId
	 * @param clientIpAddress
	 * @param deviceId
	 * @param attrNam
	 * @param oldValOfManagedAttr
	 * @param newValOfManagedAttr
	 * @param statusOfOperation
	 */
	public static synchronized void logStoreTransaction(String userId, String moduleNam, String oldValOfManagedAttr,
			String newValOfManagedAttr, Boolean statusOfOperation) {
		try {
			StoreLogMappingBeanBinaryTrade auditLogObj = new StoreLogMappingBeanBinaryTrade();
			auditLogObj.setUserId(userId);
			auditLogObj.setRole("");
			auditLogObj.setModuleName(moduleNam);
			auditLogObj.setOldValOfManagedAttr(oldValOfManagedAttr);
			auditLogObj.setNewValOfManagedAttr(newValOfManagedAttr);
			auditLogObj.setStatusOfOperation(statusOfOperation);
			auditLogObj.setDescription("");
			// new DAO().createauditlogtable(auditLogObj);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}

	}

	/**
	 * 
	 * @param userId
	 * @param clientIpAddress
	 * @param Role
	 * @param deviceId
	 * @param attrNam
	 * @param oldValOfManagedAttr
	 * @param newValOfManagedAttr
	 * @param statusOfOperation
	 * @param desc
	 * @throws Exception
	 */
	public static synchronized void logStoreTransaction(String userId, String Role, String moduleNam,
			String oldValOfManagedAttr, String newValOfManagedAttr, Boolean statusOfOperation, String desc)
			throws Exception {
		try {
			StoreLogMappingBeanBinaryTrade auditLogObj = new StoreLogMappingBeanBinaryTrade();
			auditLogObj.setUserId(userId);
			auditLogObj.setRole(Role);
			auditLogObj.setModuleName(moduleNam);
			auditLogObj.setOldValOfManagedAttr(oldValOfManagedAttr);
			auditLogObj.setNewValOfManagedAttr(newValOfManagedAttr);
			auditLogObj.setStatusOfOperation(statusOfOperation);
			auditLogObj.setDescription(desc);
			// new DAO().createauditlogtable(auditLogObj);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}

	}

	/**
	 * 
	 * @param userId
	 * @param clientIpAddress
	 * @param Role
	 * @param deviceId
	 * @param attrNam
	 * @param oldValOfManagedAttr
	 * @param newValOfManagedAttr
	 * @param statusOfOperation
	 * @param desc
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public static synchronized void auditlogTransactionBulkInsert(String userId, String Role, List uniqueId,
			String moduleNam, String oldValOfManagedAttr, String newValOfManagedAttr, Boolean statusOfOperation,
			String desc) throws Exception {

		try {
			for (int uid = 0; uid < uniqueId.size(); uid++) {
				StoreLogMappingBeanBinaryTrade auditLogObj = new StoreLogMappingBeanBinaryTrade();
				auditLogObj.setUserId(userId);
				auditLogObj.setRole(Role);
				auditLogObj.setModuleName(moduleNam);
				auditLogObj.setOldValOfManagedAttr(oldValOfManagedAttr);
				auditLogObj.setNewValOfManagedAttr(newValOfManagedAttr);
				auditLogObj.setStatusOfOperation(statusOfOperation);
				auditLogObj.setDescription(desc);
				// new DAO().createauditlogtable(auditLogObj);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}

	}

}
