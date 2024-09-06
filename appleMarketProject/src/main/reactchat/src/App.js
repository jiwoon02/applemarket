import React, {useState} from 'react';
import { SendBirdProvider } from '@sendbird/uikit-react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import MyChannelList from './MyChannelList.js';
import ChannelSelector from './ChannelSelector.js';
import GroupChannel from '@sendbird/uikit-react/GroupChannel';  // GroupChannel 임포트
import '@sendbird/uikit-react/dist/index.css';
import './App.css';

const App = () => {
  const [channelData, setChannelData] = useState({
    appId: '',
    buyerId: '',
    buyerNickname: '',
  });
  const [currentChannelUrl, setCurrentChannelUrl] = useState('');  // 채널 URL 상태 관리
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

  return (
    <SendBirdProvider
      appId={appId} 
      userId={buyerId} 
      nickname={buyerNickname}  
      colorSet={myColorSet}
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
                  onChannelSelected={handleChannelSelected} 
                />
              } 
            />
          </Routes>

          {currentChannelUrl && (
            <>
              {console.log('currentChannelUrl:', currentChannelUrl)} 
              <GroupChannel channelUrl={currentChannelUrl} />
            </>
          )}
        </div>
      </Router>
    </SendBirdProvider>
  );
};

export default App;
