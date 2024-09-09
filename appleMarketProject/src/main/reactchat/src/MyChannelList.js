import { useEffect } from 'react';

const MyChannelList = ({ setChannelData }) => {
  useEffect(() => {
    fetch('/chatroom/chatdataList')
      .then((response) => response.json())
      .then((data) => {
        setChannelData({
          appId: data.appId,
          buyerId: data.userId,
          buyerNickname: data.userNickname,
        });
      })
      .catch((error) => console.error('Error fetching MyChannelList data:', error));
  }, [setChannelData]);

  return null;
};

export default MyChannelList;