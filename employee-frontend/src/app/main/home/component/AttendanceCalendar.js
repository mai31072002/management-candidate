import React, { useEffect, useMemo } from 'react';
import { Calendar, Tag } from 'antd';
import { useDispatch, useSelector } from 'react-redux';
import * as Actions from "../store/actions";
import dayjs from 'dayjs';
import '../index.scss';

const AttendanceCalendar = ({ employeeId }) => {
    const dispatch = useDispatch();
    const month = "2025-12";

    const timeKeepingList = useSelector(
        state => state.dashboard.dashboard.timeKeeping?.timeKeeping ?? []
    );

    useEffect(() => {
        if (employeeId) {
            dispatch(Actions.fetchListTimekeeping(employeeId, month));
        }
    }, [dispatch, employeeId, month]);

    // map theo ngày
    const timeKeepingMap = useMemo(() => {
        const map = {};
        timeKeepingList.forEach(item => {
            map[dayjs(item.workDate).format('YYYY-MM-DD')] = item;
        });
        
        return map;
    }, [timeKeepingList]);


    const dateCellRender = (value) => {
        const dateStr = value.format('YYYY-MM-DD');
        
        const record = timeKeepingMap[dateStr];

        if (!record) return null;

        return (
            <div className="attendance-calendar-content">
                <Tag
                    color={record.status === 1 ? 'green' : record.status === 2 ? 'orange' : 'red'}
                    style={{ padding: '0 6px', lineHeight: '18px' }}
                >
                    {record.status === 1 ? 'Có mặt' : record.status === 2 ? 'Đi muộn về sớm' : 'Vắng'}
                </Tag>

                {record.status === 1 && (
                    <div className="attendance-calendar-content-time">
                        <span>
                            {record.checkIn.slice(0,5)}
                        </span>
                        <span> - </span>
                        <span>
                            {record.checkOut.slice(0,5)}
                        </span>
                    </div>
                )}
            </div>
        );
    };

    const cellRender = (current, info) => {
        
        if (info.type === 'date') {
            return dateCellRender(current);
        }
        return info.originNode;

    };

    return (
        <Calendar
            className="attendance-calendar"
            cellRender={cellRender}
        />
    );
};

export default AttendanceCalendar;
