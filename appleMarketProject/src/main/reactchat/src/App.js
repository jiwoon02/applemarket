import { useEffect, useState } from 'react';
import { SendBirdProvider } from '@sendbird/uikit-react';
import GroupChannel from '@sendbird/uikit-react/GroupChannel';
import ChannelSelector from './ChannelSelector.js';
import '@sendbird/uikit-react/dist/index.css';
import './App.css';

const App = () => {
  const [state, setState] = useState({});
  const [currentChannelUrl, setCurrentChannelUrl] = useState('');

  const myColorSet = {
    '--sendbird-light-primary-500': '#9D091E',
    '--sendbird-light-primary-400': '#BF0711',
    '--sendbird-light-primary-300': '#DE360B',
    '--sendbird-light-primary-200': '#F66161',
    '--sendbird-light-primary-100': '#FDAAAA',
  };

  useEffect(() => {
    const urlParams = new URLSearchParams(window.location.search);
    const productID = urlParams.get('productID');
    if (productID) {
      fetch(`/chatroom/chatdata?productID=${productID}`)
        .then(response => response.json())
        .then(data => {
          console.log('Fetched data:', data);
          setState(data);
        })
        .catch(error => console.error('Error fetching data:', error));
    }
  }, []);

  const { appId, buyerId, buyerNickname, sellerId } = state;

  if (!appId || !buyerId || !buyerNickname || !sellerId) {
    return <p>Loading...</p>;
  }

  return (
    <SendBirdProvider
      appId={appId}
      userId={buyerId}
      nickname={buyerNickname}
      colorSet={myColorSet}
    >
      {console.log('SendbirdProvider Props:', { appId, buyerId, buyerNickname })}

      <div className='app-container'>
        {buyerId && sellerId && (
          <ChannelSelector 
            buyerId={buyerId}
            sellerId={sellerId}
            onChannelSelected={(channelUrl) => setCurrentChannelUrl(channelUrl)}
          />
        )}
        {currentChannelUrl && <GroupChannel channelUrl={currentChannelUrl} />}
      </div>
    </SendBirdProvider>
  );
};

export default App;
