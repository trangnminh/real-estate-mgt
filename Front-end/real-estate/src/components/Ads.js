import React from 'react';
import AdsBanner from './AdsBanner';
import AdsCategoryCard from './AdsCategoryCard';

const Ads = () => {

    return (
        <div className="row" style={{ position: "relative", width: "90%", padding: "10px 20px", margin: "0 auto", letterSpacing: "-.2px", boxShadow: "5px 10px 8px #888888" }}>
            <div className="col-lg-6 mrb30">
                <div>
                    <div className="row">
                        <div>
                            <AdsBanner />
                        </div>
                    </div>
                </div>
            </div>

            {/* This is first element of city selection */}
            <div className="col-lg-3 mrb30">
                <div>
                    <div className="row">
                        <div>
                            <AdsCategoryCard url={"https://t1.daumcdn.net/cfile/tistory/99E8CD465C36DCB21E"} city={"Ho Chi Minh"} />
                        </div>
                        <br />
                        <div>
                            <AdsCategoryCard url={"https://a.cdn-hotels.com/gdcs/production15/d285/b90976f4-c7fd-4935-b697-d1801234452e.jpg"} city={"Ha Noi"} />
                        </div>
                    </div>
                </div>
            </div>

            {/* This is second element of city selection */}
            <div className="col-lg-2 mrb30">
                <div>
                    <h2>{ }</h2>
                    <div className="row">
                        <div>
                            <AdsCategoryCard url={"https://ww.namu.la/s/10e70d363ee30b79c6e6328083808305a1b89313b39eec103351c6dfbac8ecb2b8062c08d9577a0fdf63dd291f6e954ebfe037fac44fd69e2e11d033e6bbe1c35dc31f0f3ce446f7a49580d8840b291509b262b4c59e59d87750049f598fc5b3"} city={"Da Nang"} />
                        </div>
                        <br />
                        <div>
                            <AdsCategoryCard url={"https://media.triple.guide/triple-cms/c_limit,f_auto,h_1024,w_1024/7ab5e6ef-9b4a-49b1-9e91-49a11f9ebe62.jpeg"} city={"Ha Long Bay"} />
                        </div>
                    </div>
                </div>
            </div>

        </div>
    );
};

export default Ads;