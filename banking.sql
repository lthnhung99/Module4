use banking;
-- gửi tiền
DELIMITER //
CREATE PROCEDURE deposits(IN customerId INT, IN amount DECIMAL(12,0))
BEGIN
    DECLARE currentBalance DECIMAL(12,0);
    
    -- Lấy số dư hiện tại của khách hàng
    SELECT balance INTO currentBalance FROM customers WHERE id = customerId;
    
    -- Cộng số tiền gửi vào số dư hiện tại
    SET currentBalance = currentBalance + amount;
    
    -- Cập nhật số dư mới
    UPDATE customers SET balance = currentBalance WHERE id = customerId;
    
    -- Thêm thông tin giao dịch vào bảng deposits
    INSERT INTO deposits (created_at, created_by, customer_id, transaction_amount)
    VALUES (NOW(), customerId, customerId, amount);
END //
DELIMITER ;

-- Rút tiền
use banking;
DELIMITER //

CREATE PROCEDURE `withdraw`(IN customerId INT, IN amount DECIMAL(12,0))
BEGIN
    DECLARE currentBalance DECIMAL(12,0);
    
    -- Lấy số dư hiện tại của khách hàng
    SELECT balance INTO currentBalance FROM customers WHERE id = customerId;
    
    -- Kiểm tra số dư trước khi rút tiền
    IF currentBalance >= amount THEN
    
	-- Trừ số tiền rút khỏi số dư hiện tại
	SET currentBalance = currentBalance - amount;
        
	-- Cập nhật số dư mới
	UPDATE customers SET balance = currentBalance WHERE id = customerId;
        
	-- Thêm thông tin giao dịch vào bảng withdraws
        INSERT INTO withdraws (created_at, created_by, customer_id, transaction_amount)
        VALUES (NOW(), customerId, customerId, amount);

    END if;
END //

DELIMITER ;

-- Chuyển tiền
use banking;
DELIMITER //

CREATE PROCEDURE `transfer`(IN senderId INT, IN recipientId INT, IN amount DECIMAL(12,0))
BEGIN
    DECLARE senderBalance DECIMAL(12,0);
    DECLARE recipientBalance DECIMAL(12,0);
    DECLARE transferAmount DECIMAL(12,0);
    DECLARE fees DECIMAL(12,0);
    DECLARE feesAmount DECIMAL(12,0);
    
    -- Lấy số dư hiện tại của người gửi và người nhận
    SELECT balance INTO senderBalance FROM customers WHERE id = senderId;
    SELECT balance INTO recipientBalance FROM customers WHERE id = recipientId;
    
    -- Kiểm tra số dư người gửi trước khi chuyển khoản
    IF senderBalance >= amount THEN
	-- Tính số tiền phí
        SET fees = 10;
        SET feesAmount = (amount * fees) / 100;
        
	-- Tính số tiền chuyển khoản
        SET transferAmount = amount - feesAmount;
        
	-- Trừ số tiền chuyển khoản và phí khỏi số dư người gửi
        SET senderBalance = senderBalance - (amount + feesAmount);
        
	-- Cộng số tiền chuyển khoản vào số dư người nhận
        SET recipientBalance = recipientBalance + transferAmount;
        
	-- Cập nhật số dư mới cho người gửi và người nhận
        UPDATE customers SET balance = senderBalance WHERE id = senderId;
        UPDATE customers SET balance = recipientBalance WHERE id = recipientId;
        
	transferhistory-- Thêm thông tin giao dịch vào bảng transfers
        INSERT INTO transfers (created_at, created_by, recipient_id, sender_id, fees, fees_amount, transaction_amount, transfer_amount)
        VALUES (NOW(), senderId, recipientId, senderId, fees, feesAmount, amount, transferAmount);
    END IF;
END //

DELIMITER ;

-- lịch sử
use banking;
create view transferHistory as
select sender_id, recipient_id, fees_amount, transfer_amount, created_at 
from `transfers`;