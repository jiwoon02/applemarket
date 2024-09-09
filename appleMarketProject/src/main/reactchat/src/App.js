
import React, { useEffect, useMemo, useState } from 'react';
import { SendBirdProvider, TypingIndicatorType } from '@sendbird/uikit-react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import MyChannelList from './MyChannelList.js';
import ChannelSelector from './ChannelSelector.js';
import GroupChannel from '@sendbird/uikit-react/GroupChannel';
import '@sendbird/uikit-react/dist/index.css';
import './App.css';

const App = () => {
  const [channelData, setChannelData] = useState({
    appId: 'YOUR_APP_ID',
    buyerId: 'YOUR_USER_ID',
    buyerNickname: 'YOUR_USER_NICKNAME',
  });
  const [currentChannelUrl, setCurrentChannelUrl] = useState('');

  const { appId, buyerId, buyerNickname } = channelData;

  const myColorSet = {
    '--sendbird-light-primary-500': '#9D091E',
    '--sendbird-light-primary-400': '#BF0711',
    '--sendbird-light-primary-300': '#DE360B',
    '--sendbird-light-primary-200': '#F66161',
    '--sendbird-light-primary-100': '#FDAAAA',
  };

  const handleChannelSelected = (channelUrl) => {
    setCurrentChannelUrl(channelUrl);
  };

  const uikitOptions = useMemo(() => ({
    groupChannel: {
      enableTypingIndicator: true,
      typingIndicatorTypes: new Set([TypingIndicatorType.Bubble, TypingIndicatorType.Text]),
    }
  }), []);

  useEffect(() => {
    console.log("uikitOptions 설정값: ", uikitOptions);
  }, [uikitOptions]);

  return (
    <SendBirdProvider
      appId={appId}
      userId={buyerId}
      nickname={buyerNickname}
      colorSet={myColorSet}
      uikitOptions={uikitOptions}
    >
      <Router>
        <div className='app-container'>
          <Routes>
            <Route
              path="/chatroom/chatroom"
              element={
                <ChannelSelector
                  setChannelData={setChannelData}
                  onChannelSelected={handleChannelSelected}
                />
              }
            />
            <Route
              path="/chatroom/chatroomList"
              element={
                <MyChannelList
                  setChannelData={setChannelData}
                />
              }
            />
          </Routes>
        </div>
      </Router>
    </SendBirdProvider>
  );
};

export default App;
