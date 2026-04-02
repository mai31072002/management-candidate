# Reward & Penalty Management Function

## Overview
The Reward & Penalty Management function handles employee performance tracking, disciplinary actions, and incentive management with comprehensive approval workflows and reporting capabilities.

## Frontend Implementation

### Reward & Penalty Components
**Location**: `src/app/main/home/component/`

#### AddEditRewardPenalty Component
**File**: `src/app/main/home/component/AddEditRewardPenalty.js`

```javascript
import React, { useState, useEffect } from "react";
import { Modal, Form, Input, InputNumber, Select, DatePicker, Button } from "antd";
import { useDispatch, useSelector } from "react-redux";
import { useTranslation } from "react-i18next";
import dayjs from "dayjs";

const AddEditRewardPenalty = ({ 
    open, 
    onCancel, 
    onSubmit, 
    editingRecord, 
    employee 
}) => {
    const [form] = Form.useForm();
    const { t } = useTranslation();
    const dispatch = useDispatch();
    const { Option } = Select;

    useEffect(() => {
        if (editingRecord) {
            form.setFieldsValue({
                type: editingRecord.type,
                amount: editingRecord.amount,
                reason: editingRecord.reason,
                date: editingRecord.date ? dayjs(editingRecord.date) : null,
            });
        } else {
            form.resetFields();
            form.setFieldsValue({
                employeeId: employee?.userId,
                employeeName: employee?.employee?.fullName,
            });
        }
    }, [editingRecord, form, employee]);

    const onFinish = async (values) => {
        const reqData = {
            employeeId: values.employeeId || employee?.userId,
            type: values.type,
            amount: Number(values.amount),
            reason: values.reason.trim(),
            date: values.date.format("YYYY-MM-DD"),
        };

        onSubmit(reqData, form);
    };

    return (
        <Modal
            title={editingRecord ? t("rewardPenalty.editTitle") : t("rewardPenalty.addTitle")}
            open={open}
            onOk={() => form.submit()}
            onCancel={onCancel}
            okText={t("common.save")}
            cancelText={t("common.cancel")}
            centered
            destroyOnHidden
            width={600}
        >
            <Form form={form} layout="vertical" onFinish={onFinish}>
                <Form.Item name="employeeId" hidden>
                    <Input />
                </Form.Item>

                <Form.Item name="employeeName" label={t("rewardPenalty.employee")}>
                    <Input disabled />
                </Form.Item>

                <Form.Item
                    name="type"
                    label={t("rewardPenalty.type")}
                    rules={[
                        { required: true, message: t("rewardPenalty.typeRequired") },
                    ]}
                >
                    <Select placeholder={t("rewardPenalty.selectType")}>
                        <Option value="REWARD">{t("rewardPenalty.reward")}</Option>
                        <Option value="PENALTY">{t("rewardPenalty.penalty")}</Option>
                    </Select>
                </Form.Item>

                <Form.Item
                    name="amount"
                    label={t("rewardPenalty.amount")}
                    rules={[
                        { required: true, message: t("rewardPenalty.amountRequired") },
                        { type: "number", min: 0, message: t("rewardPenalty.amountMin") },
                        { type: "number", max: 100000000, message: t("rewardPenalty.amountMax") },
                    ]}
                >
                    <InputNumber
                        style={{ width: "100%" }}
                        formatter={value => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ",")}
                        parser={value => value.replace(/\$\s?|(,*)/g, "")}
                        placeholder={t("rewardPenalty.amountPlaceholder")}
                        min={0}
                        max={100000000}
                        step={100000}
                    />
                </Form.Item>

                <Form.Item
                    name="date"
                    label={t("rewardPenalty.date")}
                    rules={[
                        { required: true, message: t("rewardPenalty.dateRequired") },
                    ]}
                >
                    <DatePicker
                        style={{ width: "100%" }}
                        format="DD/MM/YYYY"
                        placeholder={t("rewardPenalty.selectDate")}
                        disabledDate={(current) => {
                            return current && current > dayjs().endOf("day");
                        }}
                    />
                </Form.Item>

                <Form.Item
                    name="reason"
                    label={t("rewardPenalty.reason")}
                    rules={[
                        { required: true, message: t("rewardPenalty.reasonRequired") },
```
RewardPenalty Component → Redux Action (reward_penalty.action.js) → 
Redux Store → Axios Request → Backend Controller (RewardPenaltyController.java) → 
Service Layer (RewardPenaltyService.java) → Repository Layer → Database (MySQL)
