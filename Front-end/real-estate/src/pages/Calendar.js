import React from 'react';
import { ScheduleComponent, Week, Month, ViewsDirective, ViewDirective, Inject } from '@syncfusion/ej2-react-schedule'
import SidebarNav from '../components/SidebarNav';

const Calendar = () => {

    return (
        <div className="col-lg-12 mrb30">
            <SidebarNav />
            <br />
            <ScheduleComponent currentView='Month' selectedDate={new Date()} height='850px' style={{ marginLeft: "250px" }}>
                <ViewsDirective>
                    <ViewDirective option='Week' />
                    <ViewDirective option='Month' />
                </ViewsDirective>
                <Inject services={[Week, Month]} />
            </ScheduleComponent>
        </div>
    );
};

export default Calendar;